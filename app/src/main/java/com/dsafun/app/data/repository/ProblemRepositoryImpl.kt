package com.dsafun.app.data.repository

import android.app.Application
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.dao.UserSolutionDao
import com.dsafun.app.data.local.entity.ProblemEntity
import com.dsafun.app.data.local.entity.UserSolutionEntity
import com.dsafun.app.domain.model.Problem
import com.dsafun.app.domain.model.ProblemFilter
import com.dsafun.app.domain.repository.MarkedProblemItem
import com.dsafun.app.domain.repository.ProblemRepository
import com.dsafun.app.ui.screens.LeetCodeProblemItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

private const val LEETCODE_LANGUAGE = "LEETCODE"

class ProblemRepositoryImpl @Inject constructor(
    private val application: Application,
    private val problemDao: ProblemDao,
    private val userSolutionDao: UserSolutionDao
) : ProblemRepository {

    override fun getProblems(): Flow<List<Problem>> {
        return problemDao.getAllProblems().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getFilteredProblems(filter: ProblemFilter): Flow<List<Problem>> {
        return if (!filter.isActive()) {
            getProblems()
        } else {
            problemDao.getFilteredProblems(
                topic = filter.topic,
                difficulty = null,
                searchQuery = filter.searchQuery
            ).map { entities ->
                entities
                    .filter { entity ->
                        filter.difficulties.isEmpty() || entity.difficulty in filter.difficulties
                    }
                    .map { it.toDomain() }
            }
        }
    }

    override fun getProblemById(id: Int): Flow<Problem?> {
        return problemDao.getProblemById(id).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getMarkedProblems(): Flow<MarkedProblemItem> {
        val leetCodeProblems = loadLeetCodeProblems()
        return combine(
            problemDao.getAllProblems(),
            userSolutionDao.getAllSolutions()
        ) { nativeProblems, allSolutions ->
            val favoriteSolutions = allSolutions.filter { it.isFavorite }
            val solvedNativeProblemIds = allSolutions
                .filter { it.language != LEETCODE_LANGUAGE && it.status == "SOLVED" }
                .map { it.problemId }
                .toSet()
            val solvedLeetCodeProblemIds = allSolutions
                .filter { it.language == LEETCODE_LANGUAGE && it.status == "SOLVED" }
                .map { it.problemId.toString() }
                .toSet()

            val favoriteNativeProblemIds = favoriteSolutions
                .filter { it.language != LEETCODE_LANGUAGE }
                .map { it.problemId }
                .toSet()
            val favoriteLeetCodeProblemIds = favoriteSolutions
                .filter { it.language == LEETCODE_LANGUAGE }
                .map { it.problemId.toString() }
                .toSet()

            MarkedProblemItem(
                nativeProblems = nativeProblems
                    .filter { it.id in favoriteNativeProblemIds || it.id in solvedNativeProblemIds }
                    .map { it.toDomain() },
                leetCodeProblems = leetCodeProblems
                    .filter { it.id in favoriteLeetCodeProblemIds || it.id in solvedLeetCodeProblemIds },
                solvedNativeProblemIds = solvedNativeProblemIds,
                solvedLeetCodeProblemIds = solvedLeetCodeProblemIds
            )
        }
    }

    override suspend fun saveSolution(solution: UserSolutionEntity) {
        userSolutionDao.upsertSolution(solution)
    }

    private fun ProblemEntity.toDomain(): Problem {
        return Problem(
            id = id,
            title = title,
            description = description,
            difficulty = difficulty,
            topic = topic,
            constraints = constraints,
            examples = examples,
            hints = hints,
            editorial = editorial,
            timeEstimateMinutes = timeEstimateMinutes,
            acceptanceRate = acceptanceRate,
            xpReward = xpReward,
            starterCode = starterCode,
            testCases = testCases
        )
    }

    private fun loadLeetCodeProblems(): List<LeetCodeProblemItem> {
        val reader = BufferedReader(
            InputStreamReader(application.assets.open("leetcode_problem.csv"))
        )

        val rows = mutableListOf<LeetCodeProblemItem>()
        val fields = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var isFirstRecord = true

        reader.use { bufferedReader ->
            while (true) {
                val nextChar = bufferedReader.read()
                if (nextChar == -1) break

                val char = nextChar.toChar()
                when {
                    char == '"' -> {
                        if (inQuotes) {
                            bufferedReader.mark(1)
                            val peek = bufferedReader.read()
                            if (peek == '"'.code) {
                                current.append('"')
                            } else {
                                inQuotes = false
                                if (peek != -1) bufferedReader.reset()
                            }
                        } else {
                            inQuotes = true
                        }
                    }

                    char == ',' && !inQuotes -> {
                        fields.add(current.toString())
                        current.clear()
                    }

                    (char == '\n' || char == '\r') && !inQuotes -> {
                        if (char == '\r') {
                            bufferedReader.mark(1)
                            val peek = bufferedReader.read()
                            if (peek != '\n'.code && peek != -1) bufferedReader.reset()
                        }

                        fields.add(current.toString())
                        current.clear()

                        if (isFirstRecord) {
                            isFirstRecord = false
                        } else if (fields.size >= 15) {
                            rows.add(
                                LeetCodeProblemItem(
                                    id = fields.getOrElse(1) { "" }.trim(),
                                    title = fields.getOrElse(2) { "" }.trim(),
                                    description = fields.getOrElse(3) { "" }
                                        .replace("`", "")
                                        .replace("\n", " ")
                                        .replace(Regex("\\s+"), " ")
                                        .trim(),
                                    difficulty = fields.getOrElse(5) { "" }.trim(),
                                    acceptanceRate = fields.getOrElse(7) { "" }.trim(),
                                    url = fields.getOrElse(9) { "" }.trim(),
                                    topics = fields.getOrElse(14) { "" }
                                        .split(",")
                                        .map { it.trim() }
                                        .filter { it.isNotBlank() }
                                )
                            )
                        }

                        fields.clear()
                    }

                    else -> current.append(char)
                }
            }
        }

        if (current.isNotEmpty() || fields.isNotEmpty()) {
            fields.add(current.toString())
            if (!isFirstRecord && fields.size >= 15) {
                rows.add(
                    LeetCodeProblemItem(
                        id = fields.getOrElse(1) { "" }.trim(),
                        title = fields.getOrElse(2) { "" }.trim(),
                        description = fields.getOrElse(3) { "" }
                            .replace("`", "")
                            .replace("\n", " ")
                            .replace(Regex("\\s+"), " ")
                            .trim(),
                        difficulty = fields.getOrElse(5) { "" }.trim(),
                        acceptanceRate = fields.getOrElse(7) { "" }.trim(),
                        url = fields.getOrElse(9) { "" }.trim(),
                        topics = fields.getOrElse(14) { "" }
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotBlank() }
                    )
                )
            }
        }

        return rows.filter { it.title.isNotBlank() && it.url.startsWith("http") }
    }
}

// Made with Bob
