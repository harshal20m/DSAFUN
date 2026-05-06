package com.dsafun.app.domain.model

data class ProblemFilter(
    val topic: String? = null,
    val difficulties: Set<String> = emptySet(),
    val searchQuery: String = ""
) {
    companion object {
        val EMPTY = ProblemFilter()
        
        val ALL_TOPICS = listOf("Array", "String", "Tree", "Graph", "DP")
        val ALL_DIFFICULTIES = listOf("Easy", "Medium", "Hard")
    }
    
    fun isActive(): Boolean {
        return topic != null || difficulties.isNotEmpty() || searchQuery.isNotEmpty()
    }
    
    fun clear(): ProblemFilter {
        return EMPTY
    }
}

// Made with Bob