package com.dsafun.app.domain.executor

import com.dsafun.app.data.local.model.TestCase
import com.dsafun.app.domain.model.Language
import kotlinx.coroutines.delay
import kotlin.random.Random

/**
 * Mock test case runner - simulates code execution
 * NOTE: This is a simulation. Real execution requires a sandbox environment.
 */
class TestCaseRunner {
    
    /**
     * Runs test cases against user code (simulated)
     */
    suspend fun runTests(
        code: String,
        testCases: List<TestCase>,
        language: Language,
        problemId: Int
    ): TestRunResult {
        val startTime = System.currentTimeMillis()
        
        // Simulate execution delay (200-800ms)
        delay(Random.nextLong(200, 800))
        
        val results = testCases.map { testCase ->
            evaluateTestCase(code, testCase, language, problemId)
        }
        
        val totalTime = System.currentTimeMillis() - startTime
        
        return TestRunResult(
            results = results,
            totalTimeMs = totalTime,
            isSimulated = true
        )
    }
    
    /**
     * Evaluates a single test case (mock logic)
     */
    private fun evaluateTestCase(
        code: String,
        testCase: TestCase,
        language: Language,
        problemId: Int
    ): TestCaseResult {
        // Empty code always fails
        if (code.isBlank()) {
            return TestCaseResult(
                input = testCase.input,
                expectedOutput = testCase.expectedOutput,
                actualOutput = "",
                passed = false,
                executionTimeMs = 0,
                consoleOutput = "",
                errorMessage = "SyntaxError: No code provided"
            )
        }
        
        // Check for return statement (basic validation)
        val hasReturn = when (language) {
            Language.PYTHON -> code.contains("return ")
            Language.JAVA, Language.KOTLIN -> code.contains("return ")
            Language.JAVASCRIPT -> code.contains("return ")
            Language.CPP -> code.contains("return ")
        }
        
        if (!hasReturn) {
            val errorMsg = when (language) {
                Language.PYTHON -> "SyntaxError: 'return' statement missing in function"
                Language.JAVA, Language.KOTLIN -> "CompileError: Missing return statement"
                Language.JAVASCRIPT -> "SyntaxError: Missing return statement"
                Language.CPP -> "error: no return statement in function returning non-void"
            }
            return TestCaseResult(
                input = testCase.input,
                expectedOutput = testCase.expectedOutput,
                actualOutput = "",
                passed = false,
                executionTimeMs = 0,
                consoleOutput = "",
                errorMessage = errorMsg
            )
        }
        
        // Mock: Extract print/console.log statements for console output
        val consoleOutput = extractConsoleOutput(code, language)
        
        // Check for problem-specific solution patterns
        val hasSolution = checkSolutionPattern(code, language, problemId)
        
        return if (hasSolution) {
            TestCaseResult(
                input = testCase.input,
                expectedOutput = testCase.expectedOutput,
                actualOutput = testCase.expectedOutput, // Mock: return expected
                passed = true,
                executionTimeMs = Random.nextLong(5, 50),
                consoleOutput = consoleOutput,
                errorMessage = null
            )
        } else {
            // Simulate wrong answer
            TestCaseResult(
                input = testCase.input,
                expectedOutput = testCase.expectedOutput,
                actualOutput = generateWrongOutput(testCase.expectedOutput),
                passed = false,
                executionTimeMs = Random.nextLong(5, 50),
                consoleOutput = consoleOutput,
                errorMessage = null
            )
        }
    }
    
    /**
     * Checks if code contains solution patterns for known problems
     */
    private fun checkSolutionPattern(code: String, language: Language, problemId: Int): Boolean {
        // Hardcoded patterns for the 10 seeded problems
        return when (problemId) {
            1 -> { // Two Sum
                when (language) {
                    Language.PYTHON -> code.contains("def twoSum") || code.contains("def two_sum")
                    Language.JAVA -> code.contains("int[] twoSum") || code.contains("public int[] twoSum")
                    Language.KOTLIN -> code.contains("fun twoSum")
                    Language.JAVASCRIPT -> code.contains("function twoSum") || code.contains("const twoSum")
                    Language.CPP -> code.contains("vector<int> twoSum")
                }
            }
            2 -> { // Valid Parentheses
                when (language) {
                    Language.PYTHON -> code.contains("def isValid") || code.contains("def is_valid")
                    Language.JAVA -> code.contains("boolean isValid")
                    Language.KOTLIN -> code.contains("fun isValid")
                    Language.JAVASCRIPT -> code.contains("function isValid") || code.contains("const isValid")
                    Language.CPP -> code.contains("bool isValid")
                }
            }
            3 -> { // Longest Substring
                when (language) {
                    Language.PYTHON -> code.contains("def lengthOfLongestSubstring")
                    Language.JAVA -> code.contains("int lengthOfLongestSubstring")
                    Language.KOTLIN -> code.contains("fun lengthOfLongestSubstring")
                    Language.JAVASCRIPT -> code.contains("function lengthOfLongestSubstring")
                    Language.CPP -> code.contains("int lengthOfLongestSubstring")
                }
            }
            4 -> { // Reverse String
                when (language) {
                    Language.PYTHON -> code.contains("def reverseString")
                    Language.JAVA -> code.contains("void reverseString") || code.contains("String reverseString")
                    Language.KOTLIN -> code.contains("fun reverseString")
                    Language.JAVASCRIPT -> code.contains("function reverseString")
                    Language.CPP -> code.contains("void reverseString") || code.contains("string reverseString")
                }
            }
            5 -> { // Invert Binary Tree
                when (language) {
                    Language.PYTHON -> code.contains("def invertTree")
                    Language.JAVA -> code.contains("TreeNode invertTree")
                    Language.KOTLIN -> code.contains("fun invertTree")
                    Language.JAVASCRIPT -> code.contains("function invertTree")
                    Language.CPP -> code.contains("TreeNode* invertTree")
                }
            }
            6 -> { // Max Depth Binary Tree
                when (language) {
                    Language.PYTHON -> code.contains("def maxDepth")
                    Language.JAVA -> code.contains("int maxDepth")
                    Language.KOTLIN -> code.contains("fun maxDepth")
                    Language.JAVASCRIPT -> code.contains("function maxDepth")
                    Language.CPP -> code.contains("int maxDepth")
                }
            }
            7 -> { // Number of Islands
                when (language) {
                    Language.PYTHON -> code.contains("def numIslands")
                    Language.JAVA -> code.contains("int numIslands")
                    Language.KOTLIN -> code.contains("fun numIslands")
                    Language.JAVASCRIPT -> code.contains("function numIslands")
                    Language.CPP -> code.contains("int numIslands")
                }
            }
            8 -> { // Clone Graph
                when (language) {
                    Language.PYTHON -> code.contains("def cloneGraph")
                    Language.JAVA -> code.contains("Node cloneGraph")
                    Language.KOTLIN -> code.contains("fun cloneGraph")
                    Language.JAVASCRIPT -> code.contains("function cloneGraph")
                    Language.CPP -> code.contains("Node* cloneGraph")
                }
            }
            9 -> { // Climbing Stairs
                when (language) {
                    Language.PYTHON -> code.contains("def climbStairs")
                    Language.JAVA -> code.contains("int climbStairs")
                    Language.KOTLIN -> code.contains("fun climbStairs")
                    Language.JAVASCRIPT -> code.contains("function climbStairs")
                    Language.CPP -> code.contains("int climbStairs")
                }
            }
            10 -> { // Coin Change
                when (language) {
                    Language.PYTHON -> code.contains("def coinChange")
                    Language.JAVA -> code.contains("int coinChange")
                    Language.KOTLIN -> code.contains("fun coinChange")
                    Language.JAVASCRIPT -> code.contains("function coinChange")
                    Language.CPP -> code.contains("int coinChange")
                }
            }
            else -> false // Unknown problem
        }
    }
    
    /**
     * Generates a plausible wrong output for testing
     */
    private fun generateWrongOutput(expectedOutput: String): String {
        return when {
            expectedOutput.toIntOrNull() != null -> {
                val expected = expectedOutput.toInt()
                (expected + Random.nextInt(-5, 5)).toString()
            }
            expectedOutput.toBooleanStrictOrNull() != null -> {
                (!expectedOutput.toBoolean()).toString()
            }
            expectedOutput.startsWith("[") -> {
                "[]" // Empty array
            }
            else -> "null"
        }
    }
    
    /**
     * Mock: Extract console output from print/log statements
     */
    private fun extractConsoleOutput(code: String, language: Language): String {
        val outputs = mutableListOf<String>()
        
        when (language) {
            Language.PYTHON -> {
                // Find print() statements
                val printRegex = """print\s*\(\s*["']([^"']*)["']\s*\)""".toRegex()
                printRegex.findAll(code).forEach { match ->
                    outputs.add(match.groupValues[1])
                }
            }
            Language.JAVA, Language.KOTLIN -> {
                // Find println statements
                val printRegex = """println\s*\(\s*["']([^"']*)["']\s*\)""".toRegex()
                printRegex.findAll(code).forEach { match ->
                    outputs.add(match.groupValues[1])
                }
            }
            Language.JAVASCRIPT -> {
                // Find console.log statements
                val logRegex = """console\.log\s*\(\s*["'`]([^"'`]*)["'`]\s*\)""".toRegex()
                logRegex.findAll(code).forEach { match ->
                    outputs.add(match.groupValues[1])
                }
            }
            Language.CPP -> {
                // Find cout statements
                val coutRegex = """cout\s*<<\s*["']([^"']*)["']""".toRegex()
                coutRegex.findAll(code).forEach { match ->
                    outputs.add(match.groupValues[1])
                }
            }
        }
        
        return if (outputs.isEmpty()) "" else outputs.joinToString("\n")
    }
}

/**
 * Result of running all test cases
 */
data class TestRunResult(
    val results: List<TestCaseResult>,
    val totalTimeMs: Long,
    val isSimulated: Boolean = true
) {
    val allPassed: Boolean = results.all { it.passed }
    val passedCount: Int = results.count { it.passed }
    val totalCount: Int = results.size
}

/**
 * Result of a single test case
 */
data class TestCaseResult(
    val input: String,
    val expectedOutput: String,
    val actualOutput: String,
    val passed: Boolean,
    val executionTimeMs: Long,
    val consoleOutput: String = "", // stdout/print statements
    val errorMessage: String? = null // compilation/runtime errors
)

// Made with Bob
