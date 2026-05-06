package com.dsafun.app.data.local

import com.dsafun.app.data.local.dao.BadgeDao
import com.dsafun.app.data.local.dao.ProblemDao
import com.dsafun.app.data.local.entity.BadgeEntity
import com.dsafun.app.data.local.entity.ProblemEntity
import com.dsafun.app.data.local.model.Example
import com.dsafun.app.data.local.model.TestCase
import javax.inject.Inject

class DatabaseSeeder @Inject constructor(
    private val problemDao: ProblemDao,
    private val badgeDao: BadgeDao
) {
    suspend fun seedDatabase() {
        seedBadges()

        // ==================== ARRAY PROBLEMS (1-2) ====================

        // Problem 1: Two Sum
        val twoSum = ProblemEntity(
            id = 1,
            title = "Two Sum",
            description = """
                Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.
                
                You may assume that each input would have exactly one solution, and you may not use the same element twice.
                
                You can return the answer in any order.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 2 <= nums.length <= 10⁴
                • -10⁹ <= nums[i] <= 10⁹
                • -10⁹ <= target <= 10⁹
                • Only one valid answer exists.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [2,7,11,15], target = 9",
                    output = "[0,1]",
                    explanation = "Because nums[0] + nums[1] == 9, we return [0, 1]."
                ),
                Example(
                    input = "nums = [3,2,4], target = 6",
                    output = "[1,2]",
                    explanation = "Because nums[1] + nums[2] == 6, we return [1, 2]."
                )
            ),
            hints = listOf(
                "A really brute force way would be to search for all possible pairs of numbers but that would be too slow.",
                "Use a hash map to store complement values for O(n) time complexity."
            ),
            editorial = """
                ## Approach 1: Brute Force
                
                Loop through each element x and find if there is another value that equals to target - x.
                
                **Complexity Analysis:**
                • Time complexity: O(n²)
                • Space complexity: O(1)
                
                ## Approach 2: Hash Map (Optimal)
                
                Use a hash map to store each element's index. For each element, check if its complement exists in the map.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 49.2f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun twoSum(nums: IntArray, target: Int): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def twoSum(nums: List[int], target: int) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number[]}
 */
var twoSum = function(nums, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[2,7,11,15]\n9", expectedOutput = "[0,1]", isHidden = false),
                TestCase(input = "[3,2,4]\n6", expectedOutput = "[1,2]", isHidden = false),
                TestCase(input = "[3,3]\n6", expectedOutput = "[0,1]", isHidden = true)
            )
        )
        problemDao.insertProblem(twoSum)

        // Problem 2: Container With Most Water
        val containerWithMostWater = ProblemEntity(
            id = 2,
            title = "Container With Most Water",
            description = """
                You are given an integer array `height` of length `n`. There are `n` vertical lines drawn such that the two endpoints of the `i`th line are `(i, 0)` and `(i, height[i])`.
                
                Find two lines that together with the x-axis form a container, such that the container contains the most water.
                
                Return the maximum amount of water a container can store.
                
                Notice that you may not slant the container.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • n == height.length
                • 2 <= n <= 10⁵
                • 0 <= height[i] <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "height = [1,8,6,2,5,4,8,3,7]",
                    output = "49",
                    explanation = "The vertical lines are represented by array [1,8,6,2,5,4,8,3,7]. In this case, the max area of water the container can contain is 49."
                ),
                Example(
                    input = "height = [1,1]",
                    output = "1",
                    explanation = "The max area is 1."
                )
            ),
            hints = listOf(
                "The aim is to maximize the area formed between the vertical lines.",
                "Start with the maximum width container and move the pointer with the shorter line inward."
            ),
            editorial = """
                ## Approach: Two Pointer
                
                The area formed between the lines will always be limited by the height of the shorter line.
                
                **Algorithm:**
                1. Initialize two pointers: left at start, right at end
                2. Calculate area = min(height[left], height[right]) * (right - left)
                3. Update maxArea if current area is larger
                4. Move the pointer with smaller height inward
                5. Repeat until pointers meet
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 54.1f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun maxArea(height: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int maxArea(int[] height) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxArea(height: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} height
 * @return {number}
 */
var maxArea = function(height) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int maxArea(vector<int>& height) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1,8,6,2,5,4,8,3,7]", expectedOutput = "49", isHidden = false),
                TestCase(input = "[1,1]", expectedOutput = "1", isHidden = false)
            )
        )
        problemDao.insertProblem(containerWithMostWater)

        // ==================== STRING PROBLEMS (3-4) ====================

        // Problem 3: Valid Palindrome
        val validPalindrome = ProblemEntity(
            id = 3,
            title = "Valid Palindrome",
            description = """
                A phrase is a palindrome if, after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters, it reads the same forward and backward. Alphanumeric characters include letters and numbers.
                
                Given a string `s`, return `true` if it is a palindrome, or `false` otherwise.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 2 * 10⁵
                • s consists only of printable ASCII characters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"A man, a plan, a canal: Panama\"",
                    output = "true",
                    explanation = "\"amanaplanacanalpanama\" is a palindrome."
                ),
                Example(
                    input = "s = \"race a car\"",
                    output = "false",
                    explanation = "\"raceacar\" is not a palindrome."
                )
            ),
            hints = listOf(
                "Use two pointers to check characters from both ends.",
                "Skip non-alphanumeric characters using Character.isLetterOrDigit()."
            ),
            editorial = """
                ## Approach: Two Pointer
                
                Use two pointers to check if the string is a palindrome after cleaning.
                
                **Algorithm:**
                1. Initialize left pointer at start, right pointer at end
                2. Skip non-alphanumeric characters from both ends
                3. Compare characters (case-insensitive)
                4. If they don't match, return false
                5. Move pointers inward and repeat
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 44.3f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun isPalindrome(s: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPalindrome(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPalindrome(s: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {boolean}
 */
var isPalindrome = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPalindrome(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "\"A man, a plan, a canal: Panama\"",
                    expectedOutput = "true",
                    isHidden = false
                ),
                TestCase(input = "\"race a car\"", expectedOutput = "false", isHidden = false)
            )
        )
        problemDao.insertProblem(validPalindrome)

        // Problem 4: Longest Substring Without Repeating Characters
        val longestSubstring = ProblemEntity(
            id = 4,
            title = "Longest Substring Without Repeating Characters",
            description = """
                Given a string `s`, find the length of the longest substring without repeating characters.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 5 * 10⁴
                • s consists of English letters, digits, symbols and spaces.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"abcabcbb\"",
                    output = "3",
                    explanation = "The answer is \"abc\", with the length of 3."
                ),
                Example(
                    input = "s = \"bbbbb\"",
                    output = "1",
                    explanation = "The answer is \"b\", with the length of 1."
                ),
                Example(
                    input = "s = \"pwwkew\"",
                    output = "3",
                    explanation = "The answer is \"wke\", with the length of 3."
                )
            ),
            hints = listOf(
                "Use a sliding window approach with a hash set to track characters in the current window.",
                "When you encounter a duplicate, shrink the window from the left."
            ),
            editorial = """
                ## Approach: Sliding Window with Hash Set
                
                Use a sliding window with a hash set to track characters in the current window.
                
                **Algorithm:**
                1. Use two pointers (left and right) to represent the window
                2. Use a hash set to store characters in current window
                3. Expand window by moving right pointer
                4. If character is duplicate, shrink from left until duplicate is removed
                5. Track maximum window size
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(min(m, n)) where m is charset size
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 33.8f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun lengthOfLongestSubstring(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int lengthOfLongestSubstring(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def lengthOfLongestSubstring(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var lengthOfLongestSubstring = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int lengthOfLongestSubstring(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "\"abcabcbb\"", expectedOutput = "3", isHidden = false),
                TestCase(input = "\"bbbbb\"", expectedOutput = "1", isHidden = false),
                TestCase(input = "\"pwwkew\"", expectedOutput = "3", isHidden = false)
            )
        )
        problemDao.insertProblem(longestSubstring)

        // ==================== TREE PROBLEMS (5-6) ====================

        // Problem 5: Maximum Depth of Binary Tree
        val maxDepth = ProblemEntity(
            id = 5,
            title = "Maximum Depth of Binary Tree",
            description = """
                Given the `root` of a binary tree, return its maximum depth.
                
                A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Tree",
            constraints = """
                • The number of nodes in the tree is in the range [0, 10⁴].
                • -100 <= Node.val <= 100
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "root = [3,9,20,null,null,15,7]",
                    output = "3",
                    explanation = "The maximum depth is 3."
                ),
                Example(
                    input = "root = [1,null,2]",
                    output = "2",
                    explanation = "The maximum depth is 2."
                )
            ),
            hints = listOf(
                "Use recursion. What is the base case?",
                "The depth of a tree is 1 + max(depth of left subtree, depth of right subtree)."
            ),
            editorial = """
                ## Approach 1: Recursive DFS
                
                The depth of a tree is 1 plus the maximum depth of its left and right subtrees.
                
                **Algorithm:**
                1. Base case: if root is null, return 0
                2. Recursively find depth of left subtree
                3. Recursively find depth of right subtree
                4. Return 1 + max(left depth, right depth)
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(h) where h is height
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 74.5f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
class TreeNode(var `val`: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}

fun maxDepth(root: TreeNode?): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int maxDepth(TreeNode root) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxDepth(root: Optional[TreeNode]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {TreeNode} root
 * @return {number}
 */
var maxDepth = function(root) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int maxDepth(TreeNode* root) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3,9,20,null,null,15,7]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[1,null,2]", expectedOutput = "2", isHidden = false)
            )
        )
        problemDao.insertProblem(maxDepth)

        // Problem 6: Binary Tree Level Order Traversal
        val levelOrder = ProblemEntity(
            id = 6,
            title = "Binary Tree Level Order Traversal",
            description = """
                Given the `root` of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Tree",
            constraints = """
                • The number of nodes in the tree is in the range [0, 2000].
                • -1000 <= Node.val <= 1000
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "root = [3,9,20,null,null,15,7]",
                    output = "[[3],[9,20],[15,7]]",
                    explanation = "Level 0: [3], Level 1: [9,20], Level 2: [15,7]"
                ),
                Example(
                    input = "root = [1]",
                    output = "[[1]]",
                    explanation = "Only one node at level 0."
                )
            ),
            hints = listOf(
                "Use a queue to perform breadth-first search (BFS).",
                "Process nodes level by level."
            ),
            editorial = """
                ## Approach: BFS with Queue
                
                Use a queue to perform level-order traversal.
                
                **Algorithm:**
                1. If root is null, return empty list
                2. Initialize queue with root
                3. While queue is not empty:
                   - Get current level size
                   - Create list for current level
                   - Process all nodes at current level
                   - Add their children to queue
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(w) where w is max width
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 64.2f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun levelOrder(root: TreeNode?): List<List<Int>> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public List<List<Integer>> levelOrder(TreeNode root) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def levelOrder(root: Optional[TreeNode]) -> List[List[int]]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {TreeNode} root
 * @return {number[][]}
 */
var levelOrder = function(root) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<vector<int>> levelOrder(TreeNode* root) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[3,9,20,null,null,15,7]",
                    expectedOutput = "[[3],[9,20],[15,7]]",
                    isHidden = false
                ),
                TestCase(input = "[1]", expectedOutput = "[[1]]", isHidden = false)
            )
        )
        problemDao.insertProblem(levelOrder)

        // ==================== GRAPH PROBLEMS (7-8) ====================

        // Problem 7: Number of Islands
        val numIslands = ProblemEntity(
            id = 7,
            title = "Number of Islands",
            description = """
                Given an m x n 2D binary grid `grid` which represents a map of '1's (land) and '0's (water), return the number of islands.
                
                An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Graph",
            constraints = """
                • m == grid.length
                • n == grid[i].length
                • 1 <= m, n <= 300
                • grid[i][j] is '0' or '1'.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "grid = [[\"1\",\"1\",\"1\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"0\",\"0\"]]",
                    output = "1",
                    explanation = "There is 1 island."
                ),
                Example(
                    input = "grid = [[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"1\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"1\",\"1\"]]",
                    output = "3",
                    explanation = "There are 3 islands."
                )
            ),
            hints = listOf(
                "Use DFS or BFS to explore each island.",
                "Mark visited cells to avoid counting the same island multiple times."
            ),
            editorial = """
                ## Approach: DFS
                
                Iterate through the grid and use DFS to mark connected land cells.
                
                **Algorithm:**
                1. Initialize island count to 0
                2. Iterate through each cell in grid
                3. If cell is '1':
                   - Increment island count
                   - Use DFS to mark all connected '1's as '0'
                4. Return island count
                
                **Complexity Analysis:**
                • Time complexity: O(m × n)
                • Space complexity: O(m × n) for recursion stack
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 57.3f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun numIslands(grid: Array<CharArray>): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int numIslands(char[][] grid) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def numIslands(grid: List[List[str]]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {character[][]} grid
 * @return {number}
 */
var numIslands = function(grid) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int numIslands(vector<vector<char>>& grid) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[[\"1\",\"1\",\"1\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"0\",\"0\"]]",
                    expectedOutput = "1",
                    isHidden = false
                ),
                TestCase(
                    input = "[[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"1\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"1\",\"1\"]]",
                    expectedOutput = "3",
                    isHidden = false
                )
            )
        )
        problemDao.insertProblem(numIslands)

        // Problem 8: Find if Path Exists in Graph
        val validPath = ProblemEntity(
            id = 8,
            title = "Find if Path Exists in Graph",
            description = """
                There is a bi-directional graph with `n` vertices, where each vertex is labeled from `0` to `n - 1`. The edges in the graph are represented as a 2D integer array `edges`, where each `edges[i] = [ui, vi]` denotes a bi-directional edge between vertex `ui` and vertex `vi`.
                
                Given `edges` and the integers `n`, `source`, and `destination`, return `true` if there is a valid path from `source` to `destination`, or `false` otherwise.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Graph",
            constraints = """
                • 1 <= n <= 2 * 10⁵
                • 0 <= edges.length <= 2 * 10⁵
                • 0 <= source, destination <= n - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2",
                    output = "true",
                    explanation = "There are two paths from vertex 0 to vertex 2."
                ),
                Example(
                    input = "n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], source = 0, destination = 5",
                    output = "false",
                    explanation = "There is no path from vertex 0 to vertex 5."
                )
            ),
            hints = listOf(
                "Build an adjacency list representation of the graph.",
                "Use BFS or DFS to check if destination is reachable from source."
            ),
            editorial = """
                ## Approach: BFS/DFS
                
                Build an adjacency list and use BFS or DFS to check if there's a path.
                
                **Algorithm:**
                1. Build adjacency list from edges
                2. Use BFS/DFS starting from source
                3. Mark visited nodes
                4. If we reach destination, return true
                
                **Complexity Analysis:**
                • Time complexity: O(n + e) where e is number of edges
                • Space complexity: O(n + e)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 52.8f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun validPath(n: Int, edges: Array<IntArray>, source: Int, destination: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean validPath(int n, int[][] edges, int source, int destination) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def validPath(n: int, edges: List[List[int]], source: int, destination: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @param {number[][]} edges
 * @param {number} source
 * @param {number} destination
 * @return {boolean}
 */
var validPath = function(n, edges, source, destination) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool validPath(int n, vector<vector<int>>& edges, int source, int destination) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "3\n[[0,1],[1,2],[2,0]]\n0\n2",
                    expectedOutput = "true",
                    isHidden = false
                ),
                TestCase(
                    input = "6\n[[0,1],[0,2],[3,5],[5,4],[4,3]]\n0\n5",
                    expectedOutput = "false",
                    isHidden = false
                )
            )
        )
        problemDao.insertProblem(validPath)

        // ==================== DP PROBLEMS (9-10) ====================

        // Problem 9: Climbing Stairs
        val climbingStairs = ProblemEntity(
            id = 9,
            title = "Climbing Stairs",
            description = """
                You are climbing a staircase. It takes `n` steps to reach the top.
                
                Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
            """.trimIndent(),
            difficulty = "Easy",
            topic = "DP",
            constraints = """
                • 1 <= n <= 45
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 2",
                    output = "2",
                    explanation = "There are two ways: 1+1 or 2"
                ),
                Example(
                    input = "n = 3",
                    output = "3",
                    explanation = "Three ways: 1+1+1, 1+2, 2+1"
                )
            ),
            hints = listOf(
                "To reach step n, you could have come from step n-1 or step n-2.",
                "This is similar to the Fibonacci sequence."
            ),
            editorial = """
                ## Approach: Dynamic Programming
                
                The number of ways to reach step n is the sum of ways to reach step n-1 and step n-2.
                
                **Algorithm:**
                1. Base cases: dp[1] = 1, dp[2] = 2
                2. For each step i from 3 to n: dp[i] = dp[i-1] + dp[i-2]
                3. Return dp[n]
                
                **Optimization:** We only need the last two values using O(1) space.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 51.7f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun climbStairs(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int climbStairs(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def climbStairs(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var climbStairs = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int climbStairs(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "2", expectedOutput = "2", isHidden = false),
                TestCase(input = "3", expectedOutput = "3", isHidden = false)
            )
        )
        problemDao.insertProblem(climbingStairs)

        // Problem 10: Coin Change
        val coinChange = ProblemEntity(
            id = 10,
            title = "Coin Change",
            description = """
                You are given an integer array `coins` representing coins of different denominations and an integer `amount` representing a total amount of money.
                
                Return the fewest number of coins that you need to make up that amount. If that amount cannot be made up, return `-1`.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "DP",
            constraints = """
                • 1 <= coins.length <= 12
                • 1 <= coins[i] <= 2³¹ - 1
                • 0 <= amount <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "coins = [1,2,5], amount = 11",
                    output = "3",
                    explanation = "11 = 5 + 5 + 1"
                ),
                Example(
                    input = "coins = [2], amount = 3",
                    output = "-1",
                    explanation = "Amount 3 cannot be made with coin 2."
                )
            ),
            hints = listOf(
                "Use dynamic programming where dp[i] is the minimum coins needed for amount i.",
                "For each coin, update dp[amount] = min(dp[amount], dp[amount - coin] + 1)."
            ),
            editorial = """
                ## Approach: Dynamic Programming
                
                Use bottom-up DP where dp[i] is the minimum coins needed for amount i.
                
                **Algorithm:**
                1. Initialize dp array of size amount + 1 with infinity
                2. Set dp[0] = 0
                3. For each amount from 1 to target:
                   - For each coin:
                     - If coin <= amount:
                       - dp[amount] = min(dp[amount], dp[amount - coin] + 1)
                4. Return dp[amount] if not infinity, else -1
                
                **Complexity Analysis:**
                • Time complexity: O(amount × coins.length)
                • Space complexity: O(amount)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 42.1f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun coinChange(coins: IntArray, amount: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int coinChange(int[] coins, int amount) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def coinChange(coins: List[int], amount: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} coins
 * @param {number} amount
 * @return {number}
 */
var coinChange = function(coins, amount) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int coinChange(vector<int>& coins, int amount) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1,2,5]\n11", expectedOutput = "3", isHidden = false),
                TestCase(input = "[2]\n3", expectedOutput = "-1", isHidden = false)
            )
        )
        problemDao.insertProblem(coinChange)

        // ==================== MATH PROBLEMS (11-20) ====================

        // Problem 11: Count Digits
        val countDigits = ProblemEntity(
            id = 11,
            title = "Count Digits in a Number",
            description = """
                Given an integer `n`, return the number of digits it contains.
                
                Note: Ignore the negative sign for negative numbers — only count the digit characters.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -10⁹ <= n <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 12345",
                    output = "5",
                    explanation = "The number 12345 has 5 digits."
                ),
                Example(
                    input = "n = -987",
                    output = "3",
                    explanation = "Ignore the negative sign. -987 has 3 digits."
                )
            ),
            hints = listOf(
                "Convert to string and count characters, handling the negative sign.",
                "Alternatively, use integer division: repeatedly divide by 10 until the number becomes 0."
            ),
            editorial = """
                ## Approach: String Conversion
                
                Take absolute value of n, convert to string, and return its length.
                
                **Algorithm:**
                1. Take absolute value of n (handle n = 0 case)
                2. Convert to string
                3. Return length
                
                **Complexity Analysis:**
                • Time complexity: O(d) where d is number of digits
                • Space complexity: O(d)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countDigits(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countDigits(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countDigits(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var countDigits = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countDigits(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "12345", expectedOutput = "5", isHidden = false),
                TestCase(input = "-987", expectedOutput = "3", isHidden = false),
                TestCase(input = "0", expectedOutput = "1", isHidden = true),
                TestCase(input = "1000000000", expectedOutput = "10", isHidden = true)
            )
        )
        problemDao.insertProblem(countDigits)

        // Problem 12: Sum of Digits
        val sumDigits = ProblemEntity(
            id = 12,
            title = "Sum of Digits",
            description = """
                Given an integer `n`, return the sum of all its digits.
                
                For negative numbers, ignore the sign and sum the digits of the absolute value.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -10⁹ <= n <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 1234",
                    output = "10",
                    explanation = "1 + 2 + 3 + 4 = 10"
                ),
                Example(
                    input = "n = -456",
                    output = "15",
                    explanation = "4 + 5 + 6 = 15 (sign ignored)"
                )
            ),
            hints = listOf(
                "Use modulo 10 to extract the last digit, then divide by 10 to remove it.",
                "Remember to take the absolute value first to handle negative numbers."
            ),
            editorial = """
                ## Approach: Repeated Modulo
                
                Extract the last digit using `% 10`, add it to sum, then remove it with `/ 10`.
                
                **Algorithm:**
                1. Take absolute value of n
                2. While n > 0:
                   - Add n % 10 to sum
                   - n = n / 10
                3. Return sum
                
                **Complexity Analysis:**
                • Time complexity: O(d) where d is number of digits
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 92.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun sumDigits(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int sumDigits(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def sumDigits(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var sumDigits = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int sumDigits(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "1234", expectedOutput = "10", isHidden = false),
                TestCase(input = "-456", expectedOutput = "15", isHidden = false),
                TestCase(input = "0", expectedOutput = "0", isHidden = true),
                TestCase(input = "999", expectedOutput = "27", isHidden = true)
            )
        )
        problemDao.insertProblem(sumDigits)

        // Problem 13: Reverse Number
        val reverseNumber = ProblemEntity(
            id = 13,
            title = "Reverse a Number",
            description = """
                Given a signed 32-bit integer `n`, return the number with its digits reversed.
                
                If reversing `n` causes the value to go outside the signed 32-bit integer range [-2³¹, 2³¹ - 1], return 0.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -2³¹ <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 12345",
                    output = "54321",
                    explanation = "Reversing 12345 gives 54321."
                ),
                Example(
                    input = "n = -120",
                    output = "-21",
                    explanation = "Reversing -120 gives -21."
                )
            ),
            hints = listOf(
                "Extract the last digit using modulo, build the reversed number digit by digit.",
                "Check for overflow before multiplying by 10."
            ),
            editorial = """
                ## Approach: Digit Extraction with Overflow Check
                
                Build the reversed number by extracting digits from the right.
                
                **Algorithm:**
                1. Initialize reversed = 0
                2. While n != 0:
                   - digit = n % 10
                   - Check if reversed * 10 + digit would overflow
                   - reversed = reversed * 10 + digit
                   - n = n / 10
                3. Return reversed
                
                **Complexity Analysis:**
                • Time complexity: O(d) where d is number of digits
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 88.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun reverseNumber(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int reverseNumber(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def reverseNumber(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var reverseNumber = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int reverseNumber(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "12345", expectedOutput = "54321", isHidden = false),
                TestCase(input = "-120", expectedOutput = "-21", isHidden = false),
                TestCase(input = "0", expectedOutput = "0", isHidden = true),
                TestCase(input = "1534236469", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(reverseNumber)

        // Problem 14: Check Palindrome Number
        val checkPalindrome = ProblemEntity(
            id = 14,
            title = "Check Palindrome Number",
            description = """
                Given an integer `n`, return `true` if it is a palindrome, and `false` otherwise.
                
                An integer is a palindrome when it reads the same forward and backward.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -2³¹ <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 121",
                    output = "true",
                    explanation = "121 reads as 121 from left to right and from right to left."
                ),
                Example(
                    input = "n = -121",
                    output = "false",
                    explanation = "Negative numbers are not palindromes."
                )
            ),
            hints = listOf(
                "Negative numbers are never palindromes.",
                "You can reverse the entire number or just the second half."
            ),
            editorial = """
                ## Approach: Reverse Half the Number
                
                Reverse only the second half and compare with the first half.
                
                **Algorithm:**
                1. If n < 0, or n ends in 0 (and n != 0), return false
                2. Reverse digits until reversed >= n
                3. Check if n == reversed or n == reversed / 10
                
                **Complexity Analysis:**
                • Time complexity: O(log n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 85.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isPalindromeNumber(n: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPalindromeNumber(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPalindromeNumber(n: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {boolean}
 */
var isPalindromeNumber = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPalindromeNumber(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "121", expectedOutput = "true", isHidden = false),
                TestCase(input = "-121", expectedOutput = "false", isHidden = false),
                TestCase(input = "10", expectedOutput = "false", isHidden = false),
                TestCase(input = "0", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(checkPalindrome)

        // Problem 15: Check Prime Number
        val checkPrime = ProblemEntity(
            id = 15,
            title = "Check Prime Number",
            description = """
                Given an integer `n`, return `true` if it is a prime number, and `false` otherwise.
                
                A prime number is a natural number greater than 1 that is not a product of two smaller natural numbers.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= n <= 10⁶
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 7",
                    output = "true",
                    explanation = "7 is only divisible by 1 and itself."
                ),
                Example(
                    input = "n = 4",
                    output = "false",
                    explanation = "4 = 2 × 2."
                )
            ),
            hints = listOf(
                "You only need to check divisibility up to √n.",
                "Handle edge cases: n < 2 is not prime. 2 is the only even prime."
            ),
            editorial = """
                ## Approach: Optimized Trial Division
                
                Only check up to √n, and skip even numbers after checking 2.
                
                **Algorithm:**
                1. If n < 2, return false
                2. If n == 2, return true
                3. If n is even, return false
                4. Check all odd numbers from 3 to √n
                5. If any divides n evenly, return false
                6. Otherwise return true
                
                **Complexity Analysis:**
                • Time complexity: O(√n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 80.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun isPrime(n: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPrime(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPrime(n: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {boolean}
 */
var isPrime = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPrime(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "7", expectedOutput = "true", isHidden = false),
                TestCase(input = "4", expectedOutput = "false", isHidden = false),
                TestCase(input = "1", expectedOutput = "false", isHidden = true),
                TestCase(input = "2", expectedOutput = "true", isHidden = true),
                TestCase(input = "999983", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(checkPrime)

        // Problem 16: GCD of Two Numbers
        val gcdNumbers = ProblemEntity(
            id = 16,
            title = "GCD of Two Numbers",
            description = """
                Given two integers `a` and `b`, return their Greatest Common Divisor (GCD).
                
                The GCD of two numbers is the largest positive integer that divides both numbers without a remainder.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= a, b <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "a = 12, b = 18",
                    output = "6",
                    explanation = "Divisors of 12: 1,2,3,4,6,12. Divisors of 18: 1,2,3,6,9,18. GCD = 6."
                ),
                Example(
                    input = "a = 7, b = 13",
                    output = "1",
                    explanation = "7 and 13 are both prime. GCD = 1."
                )
            ),
            hints = listOf(
                "Use the Euclidean algorithm: GCD(a, b) = GCD(b, a % b).",
                "The base case is GCD(a, 0) = a."
            ),
            editorial = """
                ## Approach: Euclidean Algorithm
                
                Repeatedly replace (a, b) with (b, a % b) until b = 0.
                
                **Algorithm:**
                1. While b != 0:
                   - temp = b
                   - b = a % b
                   - a = temp
                2. Return a
                
                **Complexity Analysis:**
                • Time complexity: O(log(min(a, b)))
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 82.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun gcd(a: Int, b: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int gcd(int a, int b) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def gcd(a: int, b: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
var gcd = function(a, b) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int gcd(int a, int b) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "12\n18", expectedOutput = "6", isHidden = false),
                TestCase(input = "7\n13", expectedOutput = "1", isHidden = false),
                TestCase(input = "100\n75", expectedOutput = "25", isHidden = true),
                TestCase(input = "1000000000\n999999999", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(gcdNumbers)

        // Problem 17: LCM of Two Numbers
        val lcmNumbers = ProblemEntity(
            id = 17,
            title = "LCM of Two Numbers",
            description = """
                Given two integers `a` and `b`, return their Least Common Multiple (LCM).
                
                The LCM of two numbers is the smallest positive integer that is divisible by both numbers.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= a, b <= 10⁹
                • The result is guaranteed to fit in a 64-bit integer.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "a = 4, b = 6",
                    output = "12",
                    explanation = "12 is the smallest number divisible by both 4 and 6."
                ),
                Example(
                    input = "a = 7, b = 5",
                    output = "35",
                    explanation = "Since GCD(7,5) = 1, LCM = 7 × 5 = 35."
                )
            ),
            hints = listOf(
                "Use the formula: LCM(a, b) = (a * b) / GCD(a, b).",
                "Be careful about integer overflow - divide first."
            ),
            editorial = """
                ## Approach: GCD-Based Formula
                
                LCM and GCD are related: LCM(a, b) = (a × b) / GCD(a, b).
                
                **Algorithm:**
                1. Compute GCD using Euclidean algorithm
                2. Return (a / gcd) * b to avoid overflow
                
                **Complexity Analysis:**
                • Time complexity: O(log(min(a, b)))
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun lcm(a: Long, b: Long): Long {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public long lcm(long a, long b) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def lcm(a: int, b: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
var lcm = function(a, b) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    long long lcm(long long a, long long b) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "4\n6", expectedOutput = "12", isHidden = false),
                TestCase(input = "7\n5", expectedOutput = "35", isHidden = false),
                TestCase(input = "12\n18", expectedOutput = "36", isHidden = true),
                TestCase(
                    input = "1000000\n999999",
                    expectedOutput = "999999000000",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(lcmNumbers)

        // Problem 18: Armstrong Number
        val armstrongNumber = ProblemEntity(
            id = 18,
            title = "Armstrong Number",
            description = """
                An Armstrong number (also known as a narcissistic number) is a number that is equal to the sum of its own digits each raised to the power of the number of digits.
                
                Given an integer `n`, return `true` if it is an Armstrong number, and `false` otherwise.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= n <= 10⁸
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 153",
                    output = "true",
                    explanation = "153 has 3 digits. 1³ + 5³ + 3³ = 1 + 125 + 27 = 153."
                ),
                Example(
                    input = "n = 9474",
                    output = "true",
                    explanation = "9⁴ + 4⁴ + 7⁴ + 4⁴ = 6561 + 256 + 2401 + 256 = 9474."
                )
            ),
            hints = listOf(
                "First count the number of digits in n - you'll need this as the exponent.",
                "Extract each digit, raise it to that power, and sum them up."
            ),
            editorial = """
                ## Approach: Digit Extraction
                
                Count digits, then extract each digit and raise to the power equal to digit count.
                
                **Algorithm:**
                1. Count the number of digits d in n
                2. Extract each digit (using % 10 and / 10)
                3. Raise each digit to the power d and add to sum
                4. Return sum == n
                
                **Complexity Analysis:**
                • Time complexity: O(d) where d is number of digits
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 78.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun isArmstrong(n: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isArmstrong(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isArmstrong(n: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {boolean}
 */
var isArmstrong = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isArmstrong(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "153", expectedOutput = "true", isHidden = false),
                TestCase(input = "9474", expectedOutput = "true", isHidden = false),
                TestCase(input = "100", expectedOutput = "false", isHidden = false),
                TestCase(input = "1", expectedOutput = "true", isHidden = true),
                TestCase(input = "9926315", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(armstrongNumber)

        // Problem 19: Leap Year
        val leapYear = ProblemEntity(
            id = 19,
            title = "Check Leap Year",
            description = """
                Given a year, determine whether it is a leap year.
                
                A year is a leap year if:
                - It is divisible by 4, AND
                - It is either not divisible by 100, OR it is divisible by 400.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= year <= 10000
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "year = 2020",
                    output = "true",
                    explanation = "2020 is divisible by 4 and not by 100."
                ),
                Example(
                    input = "year = 1900",
                    output = "false",
                    explanation = "1900 is divisible by 100 but not by 400."
                ),
                Example(
                    input = "year = 2000",
                    output = "true",
                    explanation = "2000 is divisible by 400."
                )
            ),
            hints = listOf(
                "The rule is: leap if (year % 4 == 0 AND year % 100 != 0) OR (year % 400 == 0).",
                "Check the 400 case before the 100 case."
            ),
            editorial = """
                ## Approach: Direct Condition Check
                
                Apply the Gregorian calendar rule directly.
                
                **Algorithm:**
                The condition is: `(year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)`
                
                **Why these rules?**
                Earth's orbit is ~365.2425 days, so we add a leap day every 4 years,
                skip century years (÷100) unless divisible by 400.
                
                **Complexity Analysis:**
                • Time complexity: O(1)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 90.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isLeapYear(year: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isLeapYear(int year) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isLeapYear(year: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} year
 * @return {boolean}
 */
var isLeapYear = function(year) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isLeapYear(int year) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "2020", expectedOutput = "true", isHidden = false),
                TestCase(input = "1900", expectedOutput = "false", isHidden = false),
                TestCase(input = "2000", expectedOutput = "true", isHidden = false),
                TestCase(input = "2023", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(leapYear)

        // Problem 20: Temperature Conversion
        val temperatureConversion = ProblemEntity(
            id = 20,
            title = "Temperature Conversion",
            description = """
                Given a temperature in Celsius, convert it to Fahrenheit.
                
                The conversion formula is: **F = (C × 9/5) + 32**
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -273.15 <= celsius <= 10000.0
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "celsius = 0.0",
                    output = "32.0",
                    explanation = "The freezing point of water: 0°C = 32°F."
                ),
                Example(
                    input = "celsius = 100.0",
                    output = "212.0",
                    explanation = "The boiling point of water: 100°C = 212°F."
                )
            ),
            hints = listOf(
                "Apply the formula F = (C × 9.0 / 5.0) + 32 directly.",
                "Use floating point arithmetic to get a decimal result."
            ),
            editorial = """
                ## Approach: Direct Formula Application
                
                Apply the standard Celsius-to-Fahrenheit conversion formula.
                
                **Algorithm:**
                1. Compute fahrenheit = (celsius * 9.0 / 5.0) + 32.0
                2. Round to 2 decimal places
                3. Return result
                
                **Common Pitfall:** Use floating-point division (9.0/5.0) not integer division (9/5).
                
                **Complexity Analysis:**
                • Time complexity: O(1)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun celsiusToFahrenheit(celsius: Double): Double {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public double celsiusToFahrenheit(double celsius) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def celsiusToFahrenheit(celsius: float) -> float:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} celsius
 * @return {number}
 */
var celsiusToFahrenheit = function(celsius) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    double celsiusToFahrenheit(double celsius) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "0.0", expectedOutput = "32.0", isHidden = false),
                TestCase(input = "100.0", expectedOutput = "212.0", isHidden = false),
                TestCase(input = "37.0", expectedOutput = "98.6", isHidden = false),
                TestCase(input = "-40.0", expectedOutput = "-40.0", isHidden = true)
            )
        )
        problemDao.insertProblem(temperatureConversion)

        // ==================== STRING OPERATIONS (21-30) ====================

        // Problem 21: Count Vowels in String
        val countVowels = ProblemEntity(
            id = 21,
            title = "Count Vowels in String",
            description = """
                Given a string `s`, count the number of vowels (a, e, i, o, u) it contains.
                
                Consider both uppercase and lowercase vowels. For example, 'A' and 'a' both count as vowels.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of printable ASCII characters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "2",
                    explanation = "'e' and 'o' are vowels."
                ),
                Example(
                    input = "s = \"AEIOU\"",
                    output = "5",
                    explanation = "All uppercase vowels count."
                ),
                Example(
                    input = "s = \"bcdfg\"",
                    output = "0",
                    explanation = "No vowels in this string."
                )
            ),
            hints = listOf(
                "Create a set of vowels (both lowercase and uppercase).",
                "Iterate through each character and check if it's in the vowel set."
            ),
            editorial = """
                ## Approach: Set Lookup
                
                Use a set containing all vowels (a, e, i, o, u in both cases) for O(1) lookup.
                
                **Algorithm:**
                1. Create a set of vowels: a, e, i, o, u (both cases)
                2. Initialize count = 0
                3. For each character in s:
                   - If character is in vowel set, increment count
                4. Return count
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) - fixed-size vowel set
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 92.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countVowels(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countVowels(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countVowels(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var countVowels = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countVowels(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "2", isHidden = false),
                TestCase(input = "AEIOU", expectedOutput = "5", isHidden = false),
                TestCase(input = "bcdfg", expectedOutput = "0", isHidden = false),
                TestCase(input = "Hello World", expectedOutput = "3", isHidden = true)
            )
        )
        problemDao.insertProblem(countVowels)

        // Problem 22: Count Consonants in String
        val countConsonants = ProblemEntity(
            id = 22,
            title = "Count Consonants in String",
            description = """
                Given a string `s`, count the number of consonants it contains.
                
                Consonants are letters that are not vowels (a, e, i, o, u). Only count letters (a-z, A-Z), ignore digits, spaces, and punctuation.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of printable ASCII characters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "3",
                    explanation = "'h', 'l', 'l' are consonants."
                ),
                Example(
                    input = "s = \"Hello World!\"",
                    output = "7",
                    explanation = "H, l, l, W, r, l, d are consonants (ignoring space and !)."
                )
            ),
            hints = listOf(
                "A consonant is a letter that is not a vowel.",
                "Use Character.isLetter() to check if a character is a letter."
            ),
            editorial = """
                ## Approach: Filter and Count
                
                Count characters that are letters but not vowels.
                
                **Algorithm:**
                1. Create a set of vowels
                2. Initialize count = 0
                3. For each character in s:
                   - If character is a letter AND not a vowel, increment count
                4. Return count
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 90.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countConsonants(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countConsonants(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countConsonants(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var countConsonants = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countConsonants(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "3", isHidden = false),
                TestCase(input = "Hello World!", expectedOutput = "7", isHidden = false),
                TestCase(input = "aeiou", expectedOutput = "0", isHidden = false),
                TestCase(input = "12345", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(countConsonants)

        // Problem 23: Reverse a String
        val reverseString = ProblemEntity(
            id = 23,
            title = "Reverse a String",
            description = """
                Write a function that reverses a given string.
                
                The input string is given as an array of characters. You must do this by modifying the input array in-place with O(1) extra memory.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 10⁵
                • s[i] is a printable ASCII character.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "\"olleh\"",
                    explanation = "Reverse the string character by character."
                ),
                Example(
                    input = "s = \"Hannah\"",
                    output = "\"hannaH\"",
                    explanation = "Case-sensitive reversal."
                )
            ),
            hints = listOf(
                "Use two pointers: one at the beginning, one at the end.",
                "Swap characters at the two pointers and move them towards the center."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Use two pointers to swap characters from both ends moving towards the center.
                
                **Algorithm:**
                1. Initialize left = 0, right = s.length - 1
                2. While left < right:
                   - Swap s[left] and s[right]
                   - left++, right--
                3. Return the reversed string
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun reverseString(s: CharArray): Unit {
    // Write your code here (modify array in-place)
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public void reverseString(char[] s) {
        // Write your code here (modify array in-place)
        
    }
}
                """.trimIndent(),
                "Python" to """
def reverseString(s: List[str]) -> None:
    # Write your code here (modify list in-place)
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {character[]} s
 * @return {void} Do not return anything, modify s in-place instead.
 */
var reverseString = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    void reverseString(vector<char>& s) {
        // Write your code here (modify array in-place)
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "olleh", isHidden = false),
                TestCase(input = "Hannah", expectedOutput = "hannaH", isHidden = false),
                TestCase(input = "a", expectedOutput = "a", isHidden = false),
                TestCase(input = "racecar", expectedOutput = "racecar", isHidden = true)
            )
        )
        problemDao.insertProblem(reverseString)

        // Problem 24: Check String Palindrome
        val checkStringPalindrome = ProblemEntity(
            id = 24,
            title = "Check String Palindrome",
            description = """
                Given a string `s`, determine if it is a palindrome.
                
                A palindrome is a string that reads the same forward and backward.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 2 * 10⁵
                • s consists only of lowercase English letters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"racecar\"",
                    output = "true",
                    explanation = "racecar reads the same backwards."
                ),
                Example(
                    input = "s = \"hello\"",
                    output = "false",
                    explanation = "hello reversed is olleh, which is different."
                )
            ),
            hints = listOf(
                "Compare the string with its reverse.",
                "Or use two pointers from both ends for O(1) space."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Use two pointers to compare characters from both ends.
                
                **Algorithm:**
                1. Initialize left = 0, right = s.length - 1
                2. While left < right:
                   - If s[left] != s[right], return false
                   - left++, right--
                3. Return true
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 88.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun isStringPalindrome(s: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isStringPalindrome(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isStringPalindrome(s: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {boolean}
 */
var isStringPalindrome = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isStringPalindrome(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "racecar", expectedOutput = "true", isHidden = false),
                TestCase(input = "hello", expectedOutput = "false", isHidden = false),
                TestCase(input = "a", expectedOutput = "true", isHidden = false),
                TestCase(input = "abba", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(checkStringPalindrome)

        // Problem 25: Character Frequency
        val characterFrequency = ProblemEntity(
            id = 25,
            title = "Character Frequency",
            description = """
                Given a string `s`, return a map/dictionary containing the frequency of each character in the string.
                
                Only include characters that appear at least once.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of lowercase English letters only.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "{h=1, e=1, l=2, o=1}",
                    explanation = "Each character's count."
                ),
                Example(
                    input = "s = \"aabbcc\"",
                    output = "{a=2, b=2, c=2}",
                    explanation = "Each character appears twice."
                )
            ),
            hints = listOf(
                "Use a HashMap to store character counts.",
                "Iterate through the string and increment count for each character."
            ),
            editorial = """
                ## Approach: HashMap Counting
                
                Use a hash map to store the frequency of each character.
                
                **Algorithm:**
                1. Create an empty map
                2. For each character c in s:
                   - map[c] = map.getOrDefault(c, 0) + 1
                3. Return the map
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(k) where k is the number of unique characters
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 85.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun charFrequency(s: String): Map<Char, Int> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Map<Character, Integer> charFrequency(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
from collections import Counter

def charFrequency(s: str) -> dict:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {Object}
 */
var charFrequency = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    unordered_map<char, int> charFrequency(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "hello",
                    expectedOutput = "{h=1, e=1, l=2, o=1}",
                    isHidden = false
                ),
                TestCase(input = "aabbcc", expectedOutput = "{a=2, b=2, c=2}", isHidden = false),
                TestCase(input = "", expectedOutput = "{}", isHidden = false),
                TestCase(
                    input = "abcdef",
                    expectedOutput = "{a=1, b=1, c=1, d=1, e=1, f=1}",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(characterFrequency)

        // Problem 26: Remove Vowels from String
        val removeVowels = ProblemEntity(
            id = 26,
            title = "Remove Vowels from String",
            description = """
                Given a string `s`, remove all vowels (a, e, i, o, u) from the string.
                
                Return the resulting string. The original string should remain unchanged (create a new string).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of lowercase English letters only.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "\"hll\"",
                    explanation = "Remove 'e' and 'o'."
                ),
                Example(
                    input = "s = \"leetcode\"",
                    output = "\"ltcd\"",
                    explanation = "Remove 'e', 'e', 'o', 'e'."
                )
            ),
            hints = listOf(
                "Create a set of vowels for quick lookup.",
                "Build a new string by appending only non-vowel characters."
            ),
            editorial = """
                ## Approach: Filter and Build
                
                Iterate through the string and build a result containing only non-vowels.
                
                **Algorithm:**
                1. Create a set of vowels
                2. Initialize a StringBuilder (or list) for result
                3. For each character c in s:
                   - If c is not a vowel, append to result
                4. Return result as string
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n) for the result string
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 90.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun removeVowels(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String removeVowels(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def removeVowels(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var removeVowels = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string removeVowels(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "hll", isHidden = false),
                TestCase(input = "leetcode", expectedOutput = "ltcd", isHidden = false),
                TestCase(input = "aeiou", expectedOutput = "", isHidden = false),
                TestCase(input = "bcdfg", expectedOutput = "bcdfg", isHidden = true)
            )
        )
        problemDao.insertProblem(removeVowels)

        // Problem 27: Remove Spaces from String
        val removeSpaces = ProblemEntity(
            id = 27,
            title = "Remove Spaces from String",
            description = """
                Given a string `s`, remove all space characters (' ') from the string.
                
                Return the resulting string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of printable ASCII characters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "\"helloworld\"",
                    explanation = "Remove the space between words."
                ),
                Example(
                    input = "s = \"a b c d\"",
                    output = "\"abcd\"",
                    explanation = "Remove all spaces."
                )
            ),
            hints = listOf(
                "Use replaceAll or filter out space characters.",
                "String.replace(\" \", \"\") is the simplest approach."
            ),
            editorial = """
                ## Approach: Replace or Filter
                
                Remove all space characters from the string.
                
                **Algorithm:**
                1. Initialize a StringBuilder for result
                2. For each character c in s:
                   - If c != ' ', append to result
                3. Return result as string
                
                **Alternative:** Use built-in replace: s.replace(" ", "")
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun removeSpaces(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String removeSpaces(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def removeSpaces(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var removeSpaces = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string removeSpaces(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "helloworld", isHidden = false),
                TestCase(input = "a b c d", expectedOutput = "abcd", isHidden = false),
                TestCase(input = "nospaces", expectedOutput = "nospaces", isHidden = false),
                TestCase(input = "   ", expectedOutput = "", isHidden = true)
            )
        )
        problemDao.insertProblem(removeSpaces)

        // Problem 28: Count Words in String
        val countWords = ProblemEntity(
            id = 28,
            title = "Count Words in String",
            description = """
                Given a string `s`, count the number of words in the string.
                
                A word is defined as a sequence of non-space characters. Words are separated by one or more spaces.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of printable ASCII characters.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "2",
                    explanation = "Two words: 'hello' and 'world'."
                ),
                Example(
                    input = "s = \"  hello   world  \"",
                    output = "2",
                    explanation = "Extra spaces don't create extra words."
                )
            ),
            hints = listOf(
                "Split the string by spaces and count non-empty parts.",
                "Or iterate through string and count transitions from space to non-space."
            ),
            editorial = """
                ## Approach: Split or Iterate
                
                Count transitions from space to non-space character.
                
                **Algorithm:**
                1. Initialize count = 0
                2. Iterate through the string
                3. When encountering a non-space character where previous char was space (or start), increment count
                4. Return count
                
                **Alternative:** s.trim().split("\\s+").length
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 88.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun countWords(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countWords(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countWords(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var countWords = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countWords(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "2", isHidden = false),
                TestCase(input = "  hello   world  ", expectedOutput = "2", isHidden = false),
                TestCase(input = "", expectedOutput = "0", isHidden = false),
                TestCase(input = "one", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(countWords)

        // Problem 29: First Non-Repeating Character
        val firstNonRepeating = ProblemEntity(
            id = 29,
            title = "First Non-Repeating Character",
            description = """
                Given a string `s`, find the first non-repeating character and return its index.
                
                If no such character exists, return -1.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 10⁵
                • s consists of lowercase English letters only.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"leetcode\"",
                    output = "0",
                    explanation = "'l' is the first character that appears only once, at index 0."
                ),
                Example(
                    input = "s = \"loveleetcode\"",
                    output = "2",
                    explanation = "'v' is the first non-repeating character, at index 2."
                ),
                Example(
                    input = "s = \"aabb\"",
                    output = "-1",
                    explanation = "All characters repeat, so return -1."
                )
            ),
            hints = listOf(
                "Count the frequency of each character first.",
                "Then iterate through the string to find the first character with frequency 1."
            ),
            editorial = """
                ## Approach: Frequency Map
                
                Count frequencies, then find the first character with count 1.
                
                **Algorithm:**
                1. Create a frequency map for all characters in s
                2. Iterate through s with index i:
                   - If frequency[s[i]] == 1, return i
                3. Return -1 (no unique character found)
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) - only 26 lowercase letters
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 82.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun firstUniqChar(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int firstUniqChar(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def firstUniqChar(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var firstUniqChar = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int firstUniqChar(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "leetcode", expectedOutput = "0", isHidden = false),
                TestCase(input = "loveleetcode", expectedOutput = "2", isHidden = false),
                TestCase(input = "aabb", expectedOutput = "-1", isHidden = false),
                TestCase(input = "abcabcde", expectedOutput = "6", isHidden = true)
            )
        )
        problemDao.insertProblem(firstNonRepeating)

        // Problem 30: Largest Element in Array
        val largestElement = ProblemEntity(
            id = 30,
            title = "Find Largest Element in Array",
            description = """
                Given an array of integers, find and return the largest element.
                
                If the array is empty, return null or throw an exception (depending on language).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [3, 5, 1, 8, 2]",
                    output = "8",
                    explanation = "8 is the largest element."
                ),
                Example(
                    input = "arr = [-1, -5, -3]",
                    output = "-1",
                    explanation = "-1 is the largest (closest to zero)."
                )
            ),
            hints = listOf(
                "Track the maximum value while iterating through the array.",
                "Initialize max with the first element, then compare with each element."
            ),
            editorial = """
                ## Approach: Iterative Maximum
                
                Track the maximum value during a single pass through the array.
                
                **Algorithm:**
                1. If array is empty, handle appropriately
                2. Initialize max = arr[0]
                3. For each element in arr:
                   - If element > max, update max = element
                4. Return max
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun findLargest(arr: IntArray): Int? {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Integer findLargest(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def findLargest(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var findLargest = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int findLargest(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3, 5, 1, 8, 2]", expectedOutput = "8", isHidden = false),
                TestCase(input = "[-1, -5, -3]", expectedOutput = "-1", isHidden = false),
                TestCase(input = "[7]", expectedOutput = "7", isHidden = false),
                TestCase(input = "[10, 20, 30, 40, 50]", expectedOutput = "50", isHidden = true)
            )
        )
        problemDao.insertProblem(largestElement)

        // ==================== ARRAY OPERATIONS - BASIC (31-40) ====================

        // Problem 31: Find Smallest Element in Array
        val smallestElement = ProblemEntity(
            id = 31,
            title = "Find Smallest Element in Array",
            description = """
                Given an array of integers, find and return the smallest element.
                
                If the array is empty, return null or throw an exception (depending on language).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [3, 5, 1, 8, 2]",
                    output = "1",
                    explanation = "1 is the smallest element."
                ),
                Example(
                    input = "arr = [-5, -1, -3]",
                    output = "-5",
                    explanation = "-5 is the smallest (most negative)."
                )
            ),
            hints = listOf(
                "Track the minimum value while iterating through the array.",
                "Initialize min with the first element, then compare with each element."
            ),
            editorial = """
                ## Approach: Iterative Minimum
                
                Track the minimum value during a single pass through the array.
                
                **Algorithm:**
                1. If array is empty, handle appropriately
                2. Initialize min = arr[0]
                3. For each element in arr:
                   - If element < min, update min = element
                4. Return min
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun findSmallest(arr: IntArray): Int? {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Integer findSmallest(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def findSmallest(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var findSmallest = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int findSmallest(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3, 5, 1, 8, 2]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[-5, -1, -3]", expectedOutput = "-5", isHidden = false),
                TestCase(input = "[7]", expectedOutput = "7", isHidden = false),
                TestCase(input = "[100, 200, 50, 300]", expectedOutput = "50", isHidden = true)
            )
        )
        problemDao.insertProblem(smallestElement)

        // Problem 32: Sum of Array Elements
        val sumArray = ProblemEntity(
            id = 32,
            title = "Sum of Array Elements",
            description = """
                Given an array of integers, calculate and return the sum of all elements.
                
                For an empty array, return 0.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5]",
                    output = "15",
                    explanation = "1 + 2 + 3 + 4 + 5 = 15"
                ),
                Example(
                    input = "arr = [-1, 0, 1]",
                    output = "0",
                    explanation = "-1 + 0 + 1 = 0"
                )
            ),
            hints = listOf(
                "Initialize sum = 0, then add each element to sum.",
                "Use a simple for loop or built-in sum function."
            ),
            editorial = """
                ## Approach: Iterative Summation
                
                Accumulate the sum of all elements by iterating through the array.
                
                **Algorithm:**
                1. Initialize sum = 0
                2. For each element in arr:
                   - sum += element
                3. Return sum
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 98.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun sumArray(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int sumArray(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def sumArray(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var sumArray = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int sumArray(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5]", expectedOutput = "15", isHidden = false),
                TestCase(input = "[-1, 0, 1]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[100, -50, 25, -75]", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(sumArray)

        // Problem 33: Average of Array Elements
        val averageArray = ProblemEntity(
            id = 33,
            title = "Average of Array Elements",
            description = """
                Given an array of integers, calculate and return the average of all elements as a double.
                
                For an empty array, return 0.0.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5]",
                    output = "3.0",
                    explanation = "Sum is 15, divided by 5 = 3.0"
                ),
                Example(
                    input = "arr = [10, 20, 30]",
                    output = "20.0",
                    explanation = "Sum is 60, divided by 3 = 20.0"
                )
            ),
            hints = listOf(
                "Calculate the sum first, then divide by the length of the array.",
                "Make sure to use floating-point division to get decimal results."
            ),
            editorial = """
                ## Approach: Sum then Divide
                
                Calculate the sum of all elements, then divide by the array length.
                
                **Algorithm:**
                1. If array is empty, return 0.0
                2. Calculate sum of all elements
                3. Return sum / arr.length as a double
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun averageArray(arr: IntArray): Double {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public double averageArray(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def averageArray(arr: List[int]) -> float:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var averageArray = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    double averageArray(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5]", expectedOutput = "3.0", isHidden = false),
                TestCase(input = "[10, 20, 30]", expectedOutput = "20.0", isHidden = false),
                TestCase(input = "[]", expectedOutput = "0.0", isHidden = false),
                TestCase(input = "[5, 10, 15, 20]", expectedOutput = "12.5", isHidden = true)
            )
        )
        problemDao.insertProblem(averageArray)

        // Problem 34: Search Element in Array
        val searchElement = ProblemEntity(
            id = 34,
            title = "Search Element in Array",
            description = """
                Given an array of integers and a target value, find the index of the target in the array.
                
                Return the first occurrence index if found, otherwise return -1.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 3",
                    output = "2",
                    explanation = "3 is at index 2 (0-based indexing)."
                ),
                Example(
                    input = "arr = [5, 3, 2, 1], target = 6",
                    output = "-1",
                    explanation = "6 is not in the array."
                )
            ),
            hints = listOf(
                "Use linear search: iterate through the array and compare each element.",
                "Return the index as soon as you find a match."
            ),
            editorial = """
                ## Approach: Linear Search
                
                Iterate through the array and return the index when the target is found.
                
                **Algorithm:**
                1. For i from 0 to arr.length - 1:
                   - If arr[i] == target, return i
                2. Return -1 (target not found)
                
                **Complexity Analysis:**
                • Time complexity: O(n) in worst case
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 92.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun searchElement(arr: IntArray, target: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int searchElement(int[] arr, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def searchElement(arr: List[int], target: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @param {number} target
 * @return {number}
 */
var searchElement = function(arr, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int searchElement(vector<int>& arr, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5]\n3", expectedOutput = "2", isHidden = false),
                TestCase(input = "[5, 3, 2, 1]\n6", expectedOutput = "-1", isHidden = false),
                TestCase(input = "[10, 20, 30, 40]\n40", expectedOutput = "3", isHidden = false),
                TestCase(input = "[1, 1, 1, 1]\n1", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(searchElement)

        // Problem 35: Count Even Numbers in Array
        val countEven = ProblemEntity(
            id = 35,
            title = "Count Even Numbers in Array",
            description = """
                Given an array of integers, count how many even numbers are in the array.
                
                An even number is divisible by 2 (number % 2 == 0).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5, 6]",
                    output = "3",
                    explanation = "2, 4, 6 are even numbers."
                ),
                Example(
                    input = "arr = [11, 13, 15]",
                    output = "0",
                    explanation = "No even numbers."
                )
            ),
            hints = listOf(
                "Use the modulo operator (%) to check if a number is even.",
                "Even numbers satisfy: num % 2 == 0"
            ),
            editorial = """
                ## Approach: Count with Modulo
                
                Iterate through the array and count elements that are divisible by 2.
                
                **Algorithm:**
                1. Initialize count = 0
                2. For each element in arr:
                   - If element % 2 == 0, increment count
                3. Return count
                
                **Note:** For negative numbers, num % 2 == 0 still works correctly.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countEven(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countEven(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countEven(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var countEven = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countEven(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5, 6]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[11, 13, 15]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[-2, -4, -6, 1, 3]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[0, 2, 4, 6, 8]", expectedOutput = "5", isHidden = true)
            )
        )
        problemDao.insertProblem(countEven)

        // Problem 36: Count Odd Numbers in Array
        val countOdd = ProblemEntity(
            id = 36,
            title = "Count Odd Numbers in Array",
            description = """
                Given an array of integers, count how many odd numbers are in the array.
                
                An odd number is not divisible by 2 (number % 2 != 0).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5, 6]",
                    output = "3",
                    explanation = "1, 3, 5 are odd numbers."
                ),
                Example(
                    input = "arr = [2, 4, 6, 8]",
                    output = "0",
                    explanation = "No odd numbers."
                )
            ),
            hints = listOf(
                "Use the modulo operator (%) to check if a number is odd.",
                "Odd numbers satisfy: num % 2 != 0"
            ),
            editorial = """
                ## Approach: Count with Modulo
                
                Iterate through the array and count elements that are not divisible by 2.
                
                **Algorithm:**
                1. Initialize count = 0
                2. For each element in arr:
                   - If element % 2 != 0, increment count
                3. Return count
                
                **Note:** For negative numbers, use absolute value or check num % 2 != 0.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countOdd(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countOdd(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countOdd(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var countOdd = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countOdd(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5, 6]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[2, 4, 6, 8]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[-1, -3, 2, 4]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[1, 3, 5, 7, 9]", expectedOutput = "5", isHidden = true)
            )
        )
        problemDao.insertProblem(countOdd)

        // Problem 37: Copy Array
        val copyArray = ProblemEntity(
            id = 37,
            title = "Copy Array",
            description = """
                Given an array of integers, create and return a copy of the array.
                
                The original array should remain unchanged.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3]",
                    output = "[1, 2, 3]",
                    explanation = "Return a new array with the same elements."
                ),
                Example(
                    input = "arr = []",
                    output = "[]",
                    explanation = "Empty array returns empty array."
                )
            ),
            hints = listOf(
                "Create a new array of the same length.",
                "Copy each element from the original array to the new array."
            ),
            editorial = """
                ## Approach: Manual Copy or Built-in
                
                Create a new array and copy elements from the original.
                
                **Algorithm:**
                1. Create a new array with the same length as original
                2. For i from 0 to arr.length - 1:
                   - newArr[i] = arr[i]
                3. Return newArr
                
                **Alternative:** Use built-in methods like clone() or copyOf()
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n) - for the new array
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 98.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun copyArray(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] copyArray(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def copyArray(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var copyArray = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> copyArray(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3]", expectedOutput = "[1, 2, 3]", isHidden = false),
                TestCase(input = "[]", expectedOutput = "[]", isHidden = false),
                TestCase(input = "[5]", expectedOutput = "[5]", isHidden = false),
                TestCase(
                    input = "[-10, 0, 10, 20]",
                    expectedOutput = "[-10, 0, 10, 20]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(copyArray)

        // Problem 38: Merge Two Arrays
        val mergeArrays = ProblemEntity(
            id = 38,
            title = "Merge Two Arrays",
            description = """
                Given two arrays of integers, merge them into one array.
                
                The merged array should contain all elements from the first array followed by all elements from the second array.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= arr1.length, arr2.length <= 10⁴
                • -10⁹ <= arr1[i], arr2[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr1 = [1, 2], arr2 = [3, 4]",
                    output = "[1, 2, 3, 4]",
                    explanation = "Concatenate the arrays."
                ),
                Example(
                    input = "arr1 = [], arr2 = [1, 2]",
                    output = "[1, 2]",
                    explanation = "Empty array results in just the second array."
                )
            ),
            hints = listOf(
                "Create a new array with size = arr1.length + arr2.length.",
                "Copy elements from arr1, then from arr2."
            ),
            editorial = """
                ## Approach: Array Concatenation
                
                Create a new array and copy elements from both input arrays.
                
                **Algorithm:**
                1. Create a new array of size arr1.length + arr2.length
                2. Copy all elements from arr1 into new array
                3. Copy all elements from arr2 into new array (appending after arr1)
                4. Return the merged array
                
                **Complexity Analysis:**
                • Time complexity: O(m + n) where m and n are array lengths
                • Space complexity: O(m + n) - for the merged array
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 92.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun mergeArrays(arr1: IntArray, arr2: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] mergeArrays(int[] arr1, int[] arr2) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def mergeArrays(arr1: List[int], arr2: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr1
 * @param {number[]} arr2
 * @return {number[]}
 */
var mergeArrays = function(arr1, arr2) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> mergeArrays(vector<int>& arr1, vector<int>& arr2) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2] [3, 4]",
                    expectedOutput = "[1, 2, 3, 4]",
                    isHidden = false
                ),
                TestCase(input = "[] [1, 2]", expectedOutput = "[1, 2]", isHidden = false),
                TestCase(input = "[5, 10] []", expectedOutput = "[5, 10]", isHidden = false),
                TestCase(
                    input = "[1, 3, 5] [2, 4, 6]",
                    expectedOutput = "[1, 3, 5, 2, 4, 6]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(mergeArrays)

        // Problem 39: Find Second Largest Element
        val secondLargest = ProblemEntity(
            id = 39,
            title = "Find Second Largest Element",
            description = """
                Given an array of integers, find and return the second largest element.
                
                If the array has less than 2 elements, return null or throw an exception. If all elements are the same, there is no second largest.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [3, 5, 1, 8, 2]",
                    output = "5",
                    explanation = "8 is largest, 5 is second largest."
                ),
                Example(
                    input = "arr = [10, 10, 10]",
                    output = "null",
                    explanation = "All elements are equal, no second largest."
                )
            ),
            hints = listOf(
                "Track both the largest and second largest values in a single pass.",
                "Initialize largest and secondLargest with appropriate values (e.g., Integer.MIN_VALUE)."
            ),
            editorial = """
                ## Approach: Track Two Maximums
                
                Keep track of the largest and second largest elements during iteration.
                
                **Algorithm:**
                1. Initialize largest = Integer.MIN_VALUE
                2. Initialize secondLargest = Integer.MIN_VALUE
                3. For each element in arr:
                   - If element > largest:
                     - secondLargest = largest
                     - largest = element
                   - Else if element > secondLargest AND element != largest:
                     - secondLargest = element
                4. If secondLargest is still MIN_VALUE, return null (no second largest)
                5. Return secondLargest
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun secondLargest(arr: IntArray): Int? {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Integer secondLargest(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def secondLargest(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number|null}
 */
var secondLargest = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int secondLargest(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3, 5, 1, 8, 2]", expectedOutput = "5", isHidden = false),
                TestCase(input = "[10, 10, 10]", expectedOutput = "null", isHidden = false),
                TestCase(input = "[1, 2]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[100, 90, 80, 70, 60]", expectedOutput = "90", isHidden = true)
            )
        )
        problemDao.insertProblem(secondLargest)

        // Problem 40: Bubble Sort
        val bubbleSort = ProblemEntity(
            id = 40,
            title = "Bubble Sort",
            description = """
                Sort an array of integers in ascending order using the Bubble Sort algorithm.
                
                Bubble Sort works by repeatedly stepping through the list, comparing adjacent elements and swapping them if they're in the wrong order.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10³
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [5, 2, 8, 1, 9]",
                    output = "[1, 2, 5, 8, 9]",
                    explanation = "Sorted in ascending order."
                ),
                Example(
                    input = "arr = [3, 2, 1]",
                    output = "[1, 2, 3]",
                    explanation = "Sorted in ascending order."
                )
            ),
            hints = listOf(
                "Compare adjacent elements (i and i+1).",
                "If arr[i] > arr[i+1], swap them.",
                "After each pass, the largest element 'bubbles up' to the end."
            ),
            editorial = """
                ## Approach: Bubble Sort Algorithm
                
                Repeatedly swap adjacent elements if they're in the wrong order.
                
                **Algorithm:**
                1. For i from 0 to n-1:
                   - For j from 0 to n-i-2:
                     - If arr[j] > arr[j+1]:
                       - Swap arr[j] and arr[j+1]
                2. Return sorted array
                
                **Optimization:** If no swaps occur in a pass, the array is sorted.
                
                **Complexity Analysis:**
                • Time complexity: O(n²) average/worst case, O(n) best case (already sorted)
                • Space complexity: O(1) - in-place sorting
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 80.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun bubbleSort(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] bubbleSort(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def bubbleSort(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var bubbleSort = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> bubbleSort(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[5, 2, 8, 1, 9]",
                    expectedOutput = "[1, 2, 5, 8, 9]",
                    isHidden = false
                ),
                TestCase(input = "[3, 2, 1]", expectedOutput = "[1, 2, 3]", isHidden = false),
                TestCase(
                    input = "[1, 2, 3, 4, 5]",
                    expectedOutput = "[1, 2, 3, 4, 5]",
                    isHidden = false
                ),
                TestCase(
                    input = "[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]",
                    expectedOutput = "[1, 2, 3, 4, 5, 6, 7, 8, 9, 10]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(bubbleSort)

        // ==================== ARRAY SORTING & ADVANCED (41-50) ====================

        // Problem 41: Selection Sort
        val selectionSort = ProblemEntity(
            id = 41,
            title = "Selection Sort",
            description = """
                Sort an array of integers in ascending order using the Selection Sort algorithm.
                
                Selection Sort works by repeatedly finding the minimum element from the unsorted part and putting it at the beginning.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10³
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [64, 25, 12, 22, 11]",
                    output = "[11, 12, 22, 25, 64]",
                    explanation = "Repeatedly select minimum and place at beginning."
                ),
                Example(
                    input = "arr = [5, 2, 8, 1, 9]",
                    output = "[1, 2, 5, 8, 9]",
                    explanation = "Sorted in ascending order."
                )
            ),
            hints = listOf(
                "Find the minimum element in the unsorted portion of the array.",
                "Swap it with the first element of the unsorted portion.",
                "Move the boundary of the unsorted portion forward."
            ),
            editorial = """
                ## Approach: Selection Sort Algorithm
                
                Repeatedly select the minimum element from the unsorted part and swap it with the first unsorted element.
                
                **Algorithm:**
                1. For i from 0 to n-1:
                   - Find index of minimum element from i to n-1
                   - Swap arr[i] with arr[minIndex]
                2. Return sorted array
                
                **Visualization:**
                - Step 1: Find minimum in entire array, swap with index 0
                - Step 2: Find minimum from index 1 to end, swap with index 1
                - Continue until array is sorted
                
                **Complexity Analysis:**
                • Time complexity: O(n²) - always makes n(n-1)/2 comparisons
                • Space complexity: O(1) - in-place sorting
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 78.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun selectionSort(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] selectionSort(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def selectionSort(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var selectionSort = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> selectionSort(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[64, 25, 12, 22, 11]",
                    expectedOutput = "[11, 12, 22, 25, 64]",
                    isHidden = false
                ),
                TestCase(
                    input = "[5, 2, 8, 1, 9]",
                    expectedOutput = "[1, 2, 5, 8, 9]",
                    isHidden = false
                ),
                TestCase(
                    input = "[1, 2, 3, 4, 5]",
                    expectedOutput = "[1, 2, 3, 4, 5]",
                    isHidden = false
                ),
                TestCase(
                    input = "[10, 9, 8, 7, 6]",
                    expectedOutput = "[6, 7, 8, 9, 10]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(selectionSort)

        // Problem 42: Insertion Sort
        val insertionSort = ProblemEntity(
            id = 42,
            title = "Insertion Sort",
            description = """
                Sort an array of integers in ascending order using the Insertion Sort algorithm.
                
                Insertion Sort works by building the final sorted array one element at a time, inserting each element into its correct position.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10³
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [5, 2, 4, 6, 1, 3]",
                    output = "[1, 2, 3, 4, 5, 6]",
                    explanation = "Insert each element into its correct position."
                ),
                Example(
                    input = "arr = [12, 11, 13, 5, 6]",
                    output = "[5, 6, 11, 12, 13]",
                    explanation = "Sorted in ascending order."
                )
            ),
            hints = listOf(
                "Start from the second element (index 1) and consider it as the 'key'.",
                "Compare the key with elements to its left and shift larger elements right.",
                "Insert the key in its correct position."
            ),
            editorial = """
                ## Approach: Insertion Sort Algorithm
                
                Build the sorted array by inserting each element into its correct position.
                
                **Algorithm:**
                1. For i from 1 to n-1:
                   - key = arr[i]
                   - j = i - 1
                   - While j >= 0 and arr[j] > key:
                     - arr[j + 1] = arr[j]
                     - j--
                   - arr[j + 1] = key
                2. Return sorted array
                
                **Complexity Analysis:**
                • Time complexity: O(n²) worst/average, O(n) best (already sorted)
                • Space complexity: O(1) - in-place sorting
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun insertionSort(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] insertionSort(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def insertionSort(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var insertionSort = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> insertionSort(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[5, 2, 4, 6, 1, 3]",
                    expectedOutput = "[1, 2, 3, 4, 5, 6]",
                    isHidden = false
                ),
                TestCase(
                    input = "[12, 11, 13, 5, 6]",
                    expectedOutput = "[5, 6, 11, 12, 13]",
                    isHidden = false
                ),
                TestCase(input = "[1, 2, 3, 4]", expectedOutput = "[1, 2, 3, 4]", isHidden = false),
                TestCase(
                    input = "[5, 4, 3, 2, 1]",
                    expectedOutput = "[1, 2, 3, 4, 5]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(insertionSort)

        // Problem 43: Remove Duplicates from Sorted Array
        val removeDuplicates = ProblemEntity(
            id = 43,
            title = "Remove Duplicates from Sorted Array",
            description = """
                Given a sorted array of integers, remove the duplicates in-place such that each unique element appears only once.
                
                Return the new length of the array. The first k elements of the array should contain the unique elements in sorted order.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 3 * 10⁴
                • Array is sorted in non-decreasing order
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 1, 2]",
                    output = "[1, 2]",
                    explanation = "Remove the duplicate 1. New length is 2."
                ),
                Example(
                    input = "arr = [0, 0, 1, 1, 1, 2, 2, 3, 3, 4]",
                    output = "[0, 1, 2, 3, 4]",
                    explanation = "Keep only unique elements. New length is 5."
                )
            ),
            hints = listOf(
                "Use two pointers: one for placing unique elements, one for iterating.",
                "Since array is sorted, duplicates are adjacent."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Use two pointers to track unique elements in-place.
                
                **Algorithm:**
                1. If array is empty, return 0
                2. Initialize writeIndex = 1
                3. For readIndex from 1 to n-1:
                   - If arr[readIndex] != arr[readIndex - 1]:
                     - arr[writeIndex] = arr[readIndex]
                     - writeIndex++
                4. Return writeIndex (new length)
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 82.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun removeDuplicates(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int removeDuplicates(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def removeDuplicates(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var removeDuplicates = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int removeDuplicates(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 1, 2]", expectedOutput = "2", isHidden = false),
                TestCase(
                    input = "[0, 0, 1, 1, 1, 2, 2, 3, 3, 4]",
                    expectedOutput = "5",
                    isHidden = false
                ),
                TestCase(input = "[1, 2, 3, 4]", expectedOutput = "4", isHidden = false),
                TestCase(input = "[1, 1, 1, 1, 1]", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(removeDuplicates)

        // Problem 44: Rotate Array
        val rotateArray = ProblemEntity(
            id = 44,
            title = "Rotate Array",
            description = """
                Given an array of integers, rotate the array to the right by k steps.
                
                For example, rotating [1, 2, 3, 4, 5] by 2 steps gives [4, 5, 1, 2, 3].
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁵
                • 0 <= k <= 10⁵
                • -2³¹ <= array[i] <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5], k = 2",
                    output = "[4, 5, 1, 2, 3]",
                    explanation = "Rotate right by 2 steps."
                ),
                Example(
                    input = "arr = [-1, -100, 3, 99], k = 2",
                    output = "[3, 99, -1, -100]",
                    explanation = "Rotate right by 2 steps."
                )
            ),
            hints = listOf(
                "Use array reversal technique to rotate in O(n) time and O(1) space.",
                "First reverse entire array, then reverse first k elements, then reverse remaining."
            ),
            editorial = """
                ## Approach: Array Reversal
                
                Use reversal technique to rotate in-place.
                
                **Algorithm:**
                1. Normalize k: k = k % n
                2. Reverse the entire array
                3. Reverse the first k elements
                4. Reverse the remaining n-k elements
                
                **Example:** [1,2,3,4,5], k=2
                - Reverse entire: [5,4,3,2,1]
                - Reverse first 2: [4,5,3,2,1]
                - Reverse remaining: [4,5,1,2,3]
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun rotateArray(arr: IntArray, k: Int): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] rotateArray(int[] arr, int k) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def rotateArray(arr: List[int], k: int) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @param {number} k
 * @return {number[]}
 */
var rotateArray = function(arr, k) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> rotateArray(vector<int>& arr, int k) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3, 4, 5]\n2",
                    expectedOutput = "[4, 5, 1, 2, 3]",
                    isHidden = false
                ),
                TestCase(
                    input = "[-1, -100, 3, 99]\n2",
                    expectedOutput = "[3, 99, -1, -100]",
                    isHidden = false
                ),
                TestCase(input = "[1, 2]\n3", expectedOutput = "[2, 1]", isHidden = false),
                TestCase(
                    input = "[1, 2, 3, 4, 5, 6]\n11",
                    expectedOutput = "[2, 3, 4, 5, 6, 1]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(rotateArray)

        // Problem 45: Find Duplicates in Array
        val findDuplicates = ProblemEntity(
            id = 45,
            title = "Find Duplicates in Array",
            description = """
                Given an array of integers where each element is in the range [1, n], find all elements that appear twice.
                
                Return the list of duplicate elements. Each element should appear at most once in the result.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • n == array.length
                • 1 <= n <= 10⁵
                • 1 <= array[i] <= n
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [4, 3, 2, 7, 8, 2, 3, 1]",
                    output = "[2, 3]",
                    explanation = "2 and 3 appear twice."
                ),
                Example(
                    input = "arr = [1, 1, 2]",
                    output = "[1]",
                    explanation = "1 appears twice."
                )
            ),
            hints = listOf(
                "Use the fact that array values are in the range [1, n] to mark visited elements.",
                "Treat array indices as markers by making numbers negative when visited."
            ),
            editorial = """
                ## Approach: Index Marking (Negative Marking)
                
                Use the array itself as a hash map by marking visited indices.
                
                **Algorithm:**
                1. Initialize result list
                2. For each element num in arr:
                   - index = abs(num) - 1
                   - If arr[index] is negative:
                     - Add abs(num) to result (already seen)
                   - Else:
                     - Mark arr[index] as negative
                3. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) excluding output
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 80.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun findDuplicates(arr: IntArray): List<Int> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public List<Integer> findDuplicates(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def findDuplicates(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var findDuplicates = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> findDuplicates(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[4, 3, 2, 7, 8, 2, 3, 1]",
                    expectedOutput = "[2, 3]",
                    isHidden = false
                ),
                TestCase(input = "[1, 1, 2]", expectedOutput = "[1]", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "[]", isHidden = false),
                TestCase(input = "[2, 2, 2, 2]", expectedOutput = "[2]", isHidden = true)
            )
        )
        problemDao.insertProblem(findDuplicates)

        // Problem 46: Reverse an Array
        val reverseArray = ProblemEntity(
            id = 46,
            title = "Reverse an Array",
            description = """
                Given an array of integers, reverse the order of its elements in-place.
                
                After reversal, the first element should become the last, the second element should become the second-last, and so on.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5]",
                    output = "[5, 4, 3, 2, 1]",
                    explanation = "Reverse the order of elements."
                ),
                Example(
                    input = "arr = [10, 20, 30]",
                    output = "[30, 20, 10]",
                    explanation = "Reversed array."
                )
            ),
            hints = listOf(
                "Use two pointers: one at the beginning and one at the end.",
                "Swap elements at both pointers and move them towards the center."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Use two pointers to swap elements from both ends.
                
                **Algorithm:**
                1. Initialize left = 0, right = arr.length - 1
                2. While left < right:
                   - Swap arr[left] and arr[right]
                   - left++
                   - right--
                3. Return reversed array
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 90.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun reverseArray(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] reverseArray(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def reverseArray(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var reverseArray = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> reverseArray(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3, 4, 5]",
                    expectedOutput = "[5, 4, 3, 2, 1]",
                    isHidden = false
                ),
                TestCase(input = "[10, 20, 30]", expectedOutput = "[30, 20, 10]", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "[1]", isHidden = false),
                TestCase(
                    input = "[1, 2, 3, 4, 5, 6]",
                    expectedOutput = "[6, 5, 4, 3, 2, 1]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(reverseArray)

        // Problem 47: Frequency of Elements
        val frequencyCount = ProblemEntity(
            id = 47,
            title = "Frequency of Elements",
            description = """
                Given an array of integers, count the frequency of each element.
                
                Return a map where keys are the elements and values are their counts.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 2, 3, 3, 3]",
                    output = "{1=1, 2=2, 3=3}",
                    explanation = "Count occurrences of each element."
                ),
                Example(
                    input = "arr = [5, 5, 5, 5]",
                    output = "{5=4}",
                    explanation = "One element appears 4 times."
                )
            ),
            hints = listOf(
                "Use a HashMap to store element-count pairs.",
                "Iterate through the array and increment the count for each element."
            ),
            editorial = """
                ## Approach: HashMap Counting
                
                Use a hash map to store frequencies of each element.
                
                **Algorithm:**
                1. Initialize an empty map
                2. For each element in arr:
                   - map[element] = map.getOrDefault(element, 0) + 1
                3. Return the map
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(k) where k is number of unique elements
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 85.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun frequencyCount(arr: IntArray): Map<Int, Int> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Map<Integer, Integer> frequencyCount(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
from collections import Counter

def frequencyCount(arr: List[int]) -> dict:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {Object}
 */
var frequencyCount = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    unordered_map<int, int> frequencyCount(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 2, 3, 3, 3]",
                    expectedOutput = "{1=1, 2=2, 3=3}",
                    isHidden = false
                ),
                TestCase(input = "[5, 5, 5, 5]", expectedOutput = "{5=4}", isHidden = false),
                TestCase(input = "[1, 2, 3]", expectedOutput = "{1=1, 2=1, 3=1}", isHidden = false),
                TestCase(
                    input = "[1, 1, 2, 2, 3, 3]",
                    expectedOutput = "{1=2, 2=2, 3=2}",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(frequencyCount)

        // Problem 48: Find Missing Number
        val missingNumber = ProblemEntity(
            id = 48,
            title = "Find Missing Number",
            description = """
                Given an array containing n distinct numbers taken from the range [0, n], find the one missing number.
                
                For example, if n = 3, the array should contain numbers from 0 to 3 (4 numbers total), but one is missing.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • n == array.length
                • 1 <= n <= 10⁴
                • array contains n distinct numbers from [0, n]
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [3, 0, 1]",
                    output = "2",
                    explanation = "n = 3, numbers 0-3 should be present. 2 is missing."
                ),
                Example(
                    input = "arr = [0, 1]",
                    output = "2",
                    explanation = "n = 2, numbers 0-2 should be present. 2 is missing."
                )
            ),
            hints = listOf(
                "Use the sum formula: sum of 0 to n is n*(n+1)/2.",
                "Subtract the actual sum from the expected sum to find the missing number."
            ),
            editorial = """
                ## Approach: Sum Formula
                
                Calculate expected sum and subtract actual sum.
                
                **Algorithm:**
                1. n = arr.length
                2. expectedSum = n * (n + 1) / 2
                3. actualSum = sum of all elements in arr
                4. Return expectedSum - actualSum
                
                **Alternative:** Use XOR (bitwise) to avoid overflow.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 82.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun missingNumber(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int missingNumber(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def missingNumber(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var missingNumber = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int missingNumber(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3, 0, 1]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[0, 1]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[0]", expectedOutput = "1", isHidden = false),
                TestCase(
                    input = "[9, 6, 4, 2, 3, 5, 7, 0, 1]",
                    expectedOutput = "8",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(missingNumber)

        // Problem 49: Find Pair with Given Sum
        val pairSum = ProblemEntity(
            id = 49,
            title = "Find Pair with Given Sum",
            description = """
                Given an array of integers and a target value, determine if there exists a pair of elements that sum to the target.
                
                Return true if such a pair exists, false otherwise.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 2 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
                • -10⁹ <= target <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 9",
                    output = "true",
                    explanation = "4 + 5 = 9"
                ),
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 10",
                    output = "false",
                    explanation = "No pair sums to 10."
                )
            ),
            hints = listOf(
                "Use a HashSet to store seen numbers while iterating.",
                "For each number, check if (target - number) exists in the set."
            ),
            editorial = """
                ## Approach: Hash Set
                
                Use a set to track seen numbers for O(n) time complexity.
                
                **Algorithm:**
                1. Initialize an empty set
                2. For each element in arr:
                   - complement = target - element
                   - If complement in set, return true
                   - Add element to set
                3. Return false (no pair found)
                
                **Alternative:** Sort and use two pointers for O(n log n)
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 78.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun hasPairSum(arr: IntArray, target: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean hasPairSum(int[] arr, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def hasPairSum(arr: List[int], target: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @param {number} target
 * @return {boolean}
 */
var hasPairSum = function(arr, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool hasPairSum(vector<int>& arr, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5]\n9", expectedOutput = "true", isHidden = false),
                TestCase(input = "[1, 2, 3, 4, 5]\n10", expectedOutput = "false", isHidden = false),
                TestCase(input = "[5]\n5", expectedOutput = "false", isHidden = false),
                TestCase(input = "[3, 3]\n6", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(pairSum)

        // Problem 50: Find Triplet with Given Sum
        val tripletSum = ProblemEntity(
            id = 50,
            title = "Find Triplet with Given Sum",
            description = """
                Given an array of integers and a target value, determine if there exists a triplet (three elements) that sum to the target.
                
                Return true if such a triplet exists, false otherwise.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 3 <= array.length <= 10³
                • -10⁹ <= array[i] <= 10⁹
                • -10⁹ <= target <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 9",
                    output = "true",
                    explanation = "2 + 3 + 4 = 9"
                ),
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 20",
                    output = "false",
                    explanation = "No triplet sums to 20."
                )
            ),
            hints = listOf(
                "Sort the array first.",
                "Fix one element, then use two pointers to find a pair for the remaining sum."
            ),
            editorial = """
                ## Approach: Sorting + Two Pointers
                
                Sort the array, then for each element, use two pointers to find a pair.
                
                **Algorithm:**
                1. Sort the array
                2. For i from 0 to n-3:
                   - left = i + 1, right = n - 1
                   - While left < right:
                     - currentSum = arr[i] + arr[left] + arr[right]
                     - If currentSum == target, return true
                     - Else if currentSum < target, left++
                     - Else right--
                3. Return false
                
                **Complexity Analysis:**
                • Time complexity: O(n²)
                • Space complexity: O(log n) to O(n) depending on sort implementation
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 70.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun hasTripletSum(arr: IntArray, target: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean hasTripletSum(int[] arr, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def hasTripletSum(arr: List[int], target: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @param {number} target
 * @return {boolean}
 */
var hasTripletSum = function(arr, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool hasTripletSum(vector<int>& arr, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3, 4, 5]\n9", expectedOutput = "true", isHidden = false),
                TestCase(input = "[1, 2, 3, 4, 5]\n20", expectedOutput = "false", isHidden = false),
                TestCase(input = "[1, 1, 1]\n3", expectedOutput = "true", isHidden = false),
                TestCase(input = "[1, 2, 3]\n7", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(tripletSum)

        // ==================== ADVANCED ARRAY ALGORITHMS (51-60) ====================

        // Problem 51: Maximum Subarray Sum (Kadane's Algorithm)
        val maxSubarraySum = ProblemEntity(
            id = 51,
            title = "Maximum Subarray Sum (Kadane's Algorithm)",
            description = """
                Given an array of integers, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.
                
                This is known as the Maximum Subarray Problem, and Kadane's Algorithm solves it efficiently in O(n) time.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁵
                • -10⁴ <= array[i] <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [-2, 1, -3, 4, -1, 2, 1, -5, 4]",
                    output = "6",
                    explanation = "The subarray [4, -1, 2, 1] has the largest sum = 6."
                ),
                Example(
                    input = "arr = [1]",
                    output = "1",
                    explanation = "Single element subarray."
                ),
                Example(
                    input = "arr = [5, 4, -1, 7, 8]",
                    output = "23",
                    explanation = "The entire array has the largest sum."
                )
            ),
            hints = listOf(
                "Track the current subarray sum and reset it to 0 if it becomes negative.",
                "Keep track of the maximum sum seen so far."
            ),
            editorial = """
                ## Approach: Kadane's Algorithm
                
                Kadane's Algorithm keeps track of the maximum sum ending at each position.
                
                **Algorithm:**
                1. Initialize maxSoFar = arr[0], maxEndingHere = arr[0]
                2. For i from 1 to n-1:
                   - maxEndingHere = max(arr[i], maxEndingHere + arr[i])
                   - maxSoFar = max(maxSoFar, maxEndingHere)
                3. Return maxSoFar
                
                **Why it works:**
                At each step, we either start a new subarray at current element or extend the previous subarray.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun maxSubarraySum(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int maxSubarraySum(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxSubarraySum(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var maxSubarraySum = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int maxSubarraySum(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[-2, 1, -3, 4, -1, 2, 1, -5, 4]",
                    expectedOutput = "6",
                    isHidden = false
                ),
                TestCase(input = "[1]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[5, 4, -1, 7, 8]", expectedOutput = "23", isHidden = false),
                TestCase(input = "[-2, -3, -1, -5]", expectedOutput = "-1", isHidden = true),
                TestCase(input = "[8, -19, 5, -4, 20]", expectedOutput = "21", isHidden = true)
            )
        )
        problemDao.insertProblem(maxSubarraySum)

        // Problem 52: Subarray with Given Sum
        val subarraySum = ProblemEntity(
            id = 52,
            title = "Subarray with Given Sum",
            description = """
                Given an array of positive integers and a target sum, find a contiguous subarray that sums to the target.
                
                Return the starting and ending indices (inclusive) of the subarray. If no such subarray exists, return [-1, -1].
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁵
                • 1 <= array[i] <= 10⁹
                • 1 <= target <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 7, 5], target = 12",
                    output = "[2, 4]",
                    explanation = "Elements from index 2 to 4 sum to 12: 3 + 7 + 5 = 12."
                ),
                Example(
                    input = "arr = [1, 2, 3, 4, 5], target = 9",
                    output = "[2, 3]",
                    explanation = "Elements from index 2 to 3 sum to 9: 3 + 4 = 9."
                )
            ),
            hints = listOf(
                "Use sliding window technique with two pointers.",
                "Since all numbers are positive, expand window when sum < target, shrink when sum > target."
            ),
            editorial = """
                ## Approach: Sliding Window
                
                Use two pointers to maintain a window that represents the current subarray.
                
                **Algorithm:**
                1. Initialize left = 0, currentSum = 0
                2. For right from 0 to n-1:
                   - currentSum += arr[right]
                   - While currentSum > target and left <= right:
                     - currentSum -= arr[left]
                     - left++
                   - If currentSum == target:
                     - Return [left, right]
                3. Return [-1, -1]
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 72.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun subarraySum(arr: IntArray, target: Int): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] subarraySum(int[] arr, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def subarraySum(arr: List[int], target: int) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @param {number} target
 * @return {number[]}
 */
var subarraySum = function(arr, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> subarraySum(vector<int>& arr, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3, 7, 5]\n12",
                    expectedOutput = "[2, 4]",
                    isHidden = false
                ),
                TestCase(input = "[1, 2, 3, 4, 5]\n9", expectedOutput = "[2, 3]", isHidden = false),
                TestCase(
                    input = "[1, 2, 3, 4, 5]\n15",
                    expectedOutput = "[0, 4]",
                    isHidden = false
                ),
                TestCase(
                    input = "[1, 2, 3, 4, 5]\n20",
                    expectedOutput = "[-1, -1]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(subarraySum)

        // Problem 53: Equilibrium Index
        val equilibriumIndex = ProblemEntity(
            id = 53,
            title = "Equilibrium Index",
            description = """
                Find the equilibrium index of an array. An equilibrium index is an index where the sum of elements to the left equals the sum of elements to the right.
                
                Return the first equilibrium index found, or -1 if none exists.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -10⁹ <= array[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 3, 5, 2, 2]",
                    output = "2",
                    explanation = "Left sum = 1+3 = 4, Right sum = 2+2 = 4. Index 2 is equilibrium."
                ),
                Example(
                    input = "arr = [1, 2, 3]",
                    output = "-1",
                    explanation = "No index satisfies the condition."
                )
            ),
            hints = listOf(
                "Calculate total sum of the array first.",
                "Iterate through the array, tracking left sum. Right sum = total - left sum - current element."
            ),
            editorial = """
                ## Approach: Prefix Sum
                
                Track left sum and calculate right sum using total sum.
                
                **Algorithm:**
                1. Calculate totalSum = sum of all elements
                2. Initialize leftSum = 0
                3. For i from 0 to n-1:
                   - rightSum = totalSum - leftSum - arr[i]
                   - If leftSum == rightSum, return i
                   - leftSum += arr[i]
                4. Return -1
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 75.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun equilibriumIndex(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int equilibriumIndex(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def equilibriumIndex(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var equilibriumIndex = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int equilibriumIndex(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 3, 5, 2, 2]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[1, 2, 3]", expectedOutput = "-1", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[-7, 1, 5, 2, -4, 3, 0]", expectedOutput = "3", isHidden = true)
            )
        )
        problemDao.insertProblem(equilibriumIndex)

        // Problem 54: Move Zeros to End
        val moveZeros = ProblemEntity(
            id = 54,
            title = "Move Zeros to End",
            description = """
                Given an array of integers, move all zeros to the end while maintaining the relative order of the non-zero elements.
                
                Perform this operation in-place.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁴
                • -2³¹ <= array[i] <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [0, 1, 0, 3, 12]",
                    output = "[1, 3, 12, 0, 0]",
                    explanation = "Non-zero elements maintain order, zeros move to end."
                ),
                Example(
                    input = "arr = [0]",
                    output = "[0]",
                    explanation = "Single element array."
                )
            ),
            hints = listOf(
                "Use a pointer to track the position where the next non-zero element should go.",
                "Swap non-zero elements with positions where zeros are found."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Use a pointer to track the boundary between non-zero and zero elements.
                
                **Algorithm:**
                1. Initialize nonZeroIndex = 0
                2. For i from 0 to n-1:
                   - If arr[i] != 0:
                     - Swap arr[i] and arr[nonZeroIndex]
                     - nonZeroIndex++
                3. Return modified array
                
                **Alternative:** First move all non-zero elements, then fill remaining with zeros.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun moveZeros(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] moveZeros(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def moveZeros(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var moveZeros = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> moveZeros(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[0, 1, 0, 3, 12]",
                    expectedOutput = "[1, 3, 12, 0, 0]",
                    isHidden = false
                ),
                TestCase(input = "[0]", expectedOutput = "[0]", isHidden = false),
                TestCase(input = "[1, 2, 3]", expectedOutput = "[1, 2, 3]", isHidden = false),
                TestCase(
                    input = "[0, 0, 0, 1, 2]",
                    expectedOutput = "[1, 2, 0, 0, 0]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(moveZeros)

        // Problem 55: Union of Two Arrays
        val unionArrays = ProblemEntity(
            id = 55,
            title = "Union of Two Arrays",
            description = """
                Given two arrays of integers, find the union of both arrays (all distinct elements from both).
                
                Return the union as an array. The order of elements doesn't matter.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= arr1.length, arr2.length <= 10⁴
                • -10⁹ <= arr1[i], arr2[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr1 = [1, 2, 3], arr2 = [2, 3, 4]",
                    output = "[1, 2, 3, 4]",
                    explanation = "All distinct elements from both arrays."
                ),
                Example(
                    input = "arr1 = [1, 1, 1], arr2 = [1, 1, 1]",
                    output = "[1]",
                    explanation = "Duplicates removed."
                )
            ),
            hints = listOf(
                "Use a HashSet to store unique elements from both arrays.",
                "Convert the set back to an array."
            ),
            editorial = """
                ## Approach: Hash Set
                
                Use a set to automatically handle duplicates.
                
                **Algorithm:**
                1. Create a HashSet
                2. Add all elements from arr1 to the set
                3. Add all elements from arr2 to the set
                4. Convert the set to an array
                5. Return the array
                
                **Complexity Analysis:**
                • Time complexity: O(m + n)
                • Space complexity: O(m + n) for the set
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 88.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun unionArrays(arr1: IntArray, arr2: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] unionArrays(int[] arr1, int[] arr2) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def unionArrays(arr1: List[int], arr2: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr1
 * @param {number[]} arr2
 * @return {number[]}
 */
var unionArrays = function(arr1, arr2) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> unionArrays(vector<int>& arr1, vector<int>& arr2) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3] [2, 3, 4]",
                    expectedOutput = "[1, 2, 3, 4]",
                    isHidden = false
                ),
                TestCase(input = "[1, 1, 1] [1, 1, 1]", expectedOutput = "[1]", isHidden = false),
                TestCase(input = "[] [1, 2, 3]", expectedOutput = "[1, 2, 3]", isHidden = false),
                TestCase(input = "[1, 2] [3, 4]", expectedOutput = "[1, 2, 3, 4]", isHidden = true)
            )
        )
        problemDao.insertProblem(unionArrays)

        // Problem 56: Intersection of Two Arrays
        val intersectionArrays = ProblemEntity(
            id = 56,
            title = "Intersection of Two Arrays",
            description = """
                Given two arrays of integers, find the intersection (common elements) of both arrays.
                
                Return the intersection as an array. Each element in the result should appear as many times as it appears in both arrays.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 0 <= arr1.length, arr2.length <= 10⁴
                • -10⁹ <= arr1[i], arr2[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr1 = [1, 2, 3], arr2 = [2, 3, 4]",
                    output = "[2, 3]",
                    explanation = "2 and 3 are common to both arrays."
                ),
                Example(
                    input = "arr1 = [1, 2, 2, 1], arr2 = [2, 2]",
                    output = "[2, 2]",
                    explanation = "Both arrays have two 2's."
                )
            ),
            hints = listOf(
                "Use a HashMap to count frequencies in one array.",
                "For elements in the second array, if they exist in the map, add to result and decrement count."
            ),
            editorial = """
                ## Approach: Frequency Map
                
                Use a map to track counts of elements in the first array.
                
                **Algorithm:**
                1. Create a map to store frequencies of arr1
                2. Initialize result list
                3. For each element in arr2:
                   - If map contains element with count > 0:
                     - Add element to result
                     - Decrement count in map
                4. Convert result to array
                
                **Complexity Analysis:**
                • Time complexity: O(m + n)
                • Space complexity: O(min(m, n))
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun intersectionArrays(arr1: IntArray, arr2: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] intersectionArrays(int[] arr1, int[] arr2) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def intersectionArrays(arr1: List[int], arr2: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr1
 * @param {number[]} arr2
 * @return {number[]}
 */
var intersectionArrays = function(arr1, arr2) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> intersectionArrays(vector<int>& arr1, vector<int>& arr2) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3] [2, 3, 4]",
                    expectedOutput = "[2, 3]",
                    isHidden = false
                ),
                TestCase(
                    input = "[1, 2, 2, 1] [2, 2]",
                    expectedOutput = "[2, 2]",
                    isHidden = false
                ),
                TestCase(input = "[1, 2] [3, 4]", expectedOutput = "[]", isHidden = false),
                TestCase(
                    input = "[4, 9, 5] [9, 4, 9, 8, 4]",
                    expectedOutput = "[4, 9]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(intersectionArrays)

        // Problem 57: Maximum Consecutive Ones
        val consecutiveOnes = ProblemEntity(
            id = 57,
            title = "Maximum Consecutive Ones",
            description = """
                Given a binary array (containing only 0s and 1s), find the maximum number of consecutive 1s in the array.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= array.length <= 10⁵
                • array[i] is either 0 or 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 1, 0, 1, 1, 1]",
                    output = "3",
                    explanation = "The last three 1s are consecutive."
                ),
                Example(
                    input = "arr = [1, 0, 1, 1, 0, 1]",
                    output = "2",
                    explanation = "Maximum consecutive 1s is 2."
                )
            ),
            hints = listOf(
                "Iterate through the array, tracking the current count of consecutive 1s.",
                "Reset count to 0 when you see a 0."
            ),
            editorial = """
                ## Approach: Single Pass Counting
                
                Track current consecutive count and maximum seen so far.
                
                **Algorithm:**
                1. Initialize maxCount = 0, currentCount = 0
                2. For each element in arr:
                   - If element == 1:
                     - currentCount++
                     - maxCount = max(maxCount, currentCount)
                   - Else:
                     - currentCount = 0
                3. Return maxCount
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 90.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun maxConsecutiveOnes(arr: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int maxConsecutiveOnes(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxConsecutiveOnes(arr: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number}
 */
var maxConsecutiveOnes = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int maxConsecutiveOnes(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 1, 0, 1, 1, 1]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[1, 0, 1, 1, 0, 1]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[0, 0, 0]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[1, 1, 1, 1, 1]", expectedOutput = "5", isHidden = true)
            )
        )
        problemDao.insertProblem(consecutiveOnes)

        // Problem 58: Best Time to Buy and Sell Stock
        val stockProfit = ProblemEntity(
            id = 58,
            title = "Best Time to Buy and Sell Stock",
            description = """
                You are given an array `prices` where `prices[i]` is the price of a given stock on the ith day.
                
                You want to maximize your profit by choosing a single day to buy one stock and a different day in the future to sell that stock.
                
                Return the maximum profit you can achieve. If no profit is possible, return 0.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= prices.length <= 10⁵
                • 0 <= prices[i] <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "prices = [7, 1, 5, 3, 6, 4]",
                    output = "5",
                    explanation = "Buy at 1 (day 2), sell at 6 (day 5), profit = 5."
                ),
                Example(
                    input = "prices = [7, 6, 4, 3, 1]",
                    output = "0",
                    explanation = "No profit possible, so return 0."
                )
            ),
            hints = listOf(
                "Track the minimum price seen so far.",
                "Calculate potential profit by subtracting current price from min price."
            ),
            editorial = """
                ## Approach: Single Pass (Kadane's Variation)
                
                Track the minimum price and maximum profit as we iterate.
                
                **Algorithm:**
                1. Initialize minPrice = infinity
                2. Initialize maxProfit = 0
                3. For each price in prices:
                   - minPrice = min(minPrice, price)
                   - maxProfit = max(maxProfit, price - minPrice)
                4. Return maxProfit
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 78.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun maxProfit(prices: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int maxProfit(int[] prices) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxProfit(prices: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} prices
 * @return {number}
 */
var maxProfit = function(prices) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int maxProfit(vector<int>& prices) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[7, 1, 5, 3, 6, 4]", expectedOutput = "5", isHidden = false),
                TestCase(input = "[7, 6, 4, 3, 1]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[1, 2]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[3, 3, 5, 0, 0, 3, 1, 4]", expectedOutput = "4", isHidden = true)
            )
        )
        problemDao.insertProblem(stockProfit)

        // Problem 59: Product of Array Except Self
        val productExceptSelf = ProblemEntity(
            id = 59,
            title = "Product of Array Except Self",
            description = """
                Given an array of integers, return an array where each element at index i is the product of all elements of the original array except the element at i.
                
                Solve it without using division and in O(n) time.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 2 <= array.length <= 10⁵
                • -30 <= array[i] <= 30
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "arr = [1, 2, 3, 4]",
                    output = "[24, 12, 8, 6]",
                    explanation = "Product of all except self: [2*3*4, 1*3*4, 1*2*4, 1*2*3]"
                ),
                Example(
                    input = "arr = [-1, 1, 0, -3, 3]",
                    output = "[0, 0, 9, 0, 0]",
                    explanation = "Product except self considering zeros."
                )
            ),
            hints = listOf(
                "Calculate prefix products and suffix products.",
                "Combine them to get the product except self without division."
            ),
            editorial = """
                ## Approach: Prefix and Suffix Products
                
                Use two passes to calculate products of elements to the left and right of each index.
                
                **Algorithm:**
                1. Create result array initialized with 1s
                2. Calculate prefix products:
                   - For i from 1 to n-1:
                     - result[i] = result[i-1] * arr[i-1]
                3. Calculate suffix products and combine:
                   - suffix = 1
                   - For i from n-1 down to 0:
                     - result[i] = result[i] * suffix
                     - suffix = suffix * arr[i]
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) excluding output
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 70.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun productExceptSelf(arr: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] productExceptSelf(int[] arr) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def productExceptSelf(arr: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} arr
 * @return {number[]}
 */
var productExceptSelf = function(arr) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> productExceptSelf(vector<int>& arr) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1, 2, 3, 4]",
                    expectedOutput = "[24, 12, 8, 6]",
                    isHidden = false
                ),
                TestCase(
                    input = "[-1, 1, 0, -3, 3]",
                    expectedOutput = "[0, 0, 9, 0, 0]",
                    isHidden = false
                ),
                TestCase(
                    input = "[2, 3, 4, 5]",
                    expectedOutput = "[60, 40, 30, 24]",
                    isHidden = false
                ),
                TestCase(input = "[1, 1, 1, 1]", expectedOutput = "[1, 1, 1, 1]", isHidden = true)
            )
        )
        problemDao.insertProblem(productExceptSelf)

        // Problem 60: Container With Most Water (Duplicate removal - fixed)
        // Note: This is a duplicate of problem 2, so we'll skip it to avoid duplication
        // Instead, let's add a different problem: Plus One

        // Problem 60: Plus One
        val plusOne = ProblemEntity(
            id = 60,
            title = "Plus One",
            description = """
                You are given a large integer represented as an array of digits, where each digit is in the range [0, 9].
                
                Increment the integer by one and return the resulting array of digits.
                
                For example, [1, 2, 3] represents 123. Adding one gives 124 -> [1, 2, 4].
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= digits.length <= 100
                • 0 <= digits[i] <= 9
                • The integer does not contain any leading zero, except the number 0 itself.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "digits = [1, 2, 3]",
                    output = "[1, 2, 4]",
                    explanation = "123 + 1 = 124"
                ),
                Example(
                    input = "digits = [9]",
                    output = "[1, 0]",
                    explanation = "9 + 1 = 10"
                ),
                Example(
                    input = "digits = [9, 9, 9]",
                    output = "[1, 0, 0, 0]",
                    explanation = "999 + 1 = 1000"
                )
            ),
            hints = listOf(
                "Process the array from right to left.",
                "Add 1 to the last digit. If it becomes 10, set to 0 and carry over to the next digit."
            ),
            editorial = """
                ## Approach: Reverse Iteration with Carry
                
                Process digits from right to left, handling carries.
                
                **Algorithm:**
                1. For i from n-1 down to 0:
                   - If digits[i] < 9:
                     - digits[i]++
                     - Return digits
                   - Else:
                     - digits[i] = 0 (carry over)
                2. If we finish the loop, we need an extra digit at the front
                3. Create new array with 1 followed by all zeros
                4. Return new array
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) or O(n) if we need a new array for carry
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 72.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun plusOne(digits: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] plusOne(int[] digits) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def plusOne(digits: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} digits
 * @return {number[]}
 */
var plusOne = function(digits) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> plusOne(vector<int>& digits) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1, 2, 3]", expectedOutput = "[1, 2, 4]", isHidden = false),
                TestCase(input = "[9]", expectedOutput = "[1, 0]", isHidden = false),
                TestCase(input = "[9, 9, 9]", expectedOutput = "[1, 0, 0, 0]", isHidden = false),
                TestCase(input = "[8, 9, 9, 9]", expectedOutput = "[9, 0, 0, 0]", isHidden = true)
            )
        )
        problemDao.insertProblem(plusOne)

        // ==================== ADVANCED ALGORITHMS & MATH (61-70) ====================

        // Problem 61: Fibonacci Number
        val fibonacciNumber = ProblemEntity(
            id = 61,
            title = "Fibonacci Number",
            description = """
                The Fibonacci numbers, commonly denoted F(n), form a sequence called the Fibonacci sequence, such that each number is the sum of the two preceding ones, starting from 0 and 1.
                
                F(0) = 0, F(1) = 1
                F(n) = F(n - 1) + F(n - 2), for n > 1
                
                Given n, calculate F(n).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 0 <= n <= 30
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 2",
                    output = "1",
                    explanation = "F(2) = F(1) + F(0) = 1 + 0 = 1"
                ),
                Example(
                    input = "n = 3",
                    output = "2",
                    explanation = "F(3) = F(2) + F(1) = 1 + 1 = 2"
                ),
                Example(
                    input = "n = 4",
                    output = "3",
                    explanation = "F(4) = F(3) + F(2) = 2 + 1 = 3"
                )
            ),
            hints = listOf(
                "Use iteration (bottom-up) instead of recursion to avoid stack overflow.",
                "You only need to track the last two Fibonacci numbers."
            ),
            editorial = """
                ## Approach 1: Recursion (Inefficient)
                
                Recursively calculate F(n) = F(n-1) + F(n-2). 
                **Time complexity:** O(2ⁿ) - very slow!
                
                ## Approach 2: Iteration (Optimal)
                
                Use bottom-up approach with O(1) space.
                
                **Algorithm:**
                1. If n == 0, return 0
                2. If n == 1, return 1
                3. Initialize a = 0, b = 1
                4. For i from 2 to n:
                   - c = a + b
                   - a = b
                   - b = c
                5. Return b
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 85.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun fibonacci(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int fibonacci(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def fibonacci(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var fibonacci = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int fibonacci(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "2", expectedOutput = "1", isHidden = false),
                TestCase(input = "3", expectedOutput = "2", isHidden = false),
                TestCase(input = "4", expectedOutput = "3", isHidden = false),
                TestCase(input = "10", expectedOutput = "55", isHidden = true),
                TestCase(input = "30", expectedOutput = "832040", isHidden = true)
            )
        )
        problemDao.insertProblem(fibonacciNumber)

        // Problem 62: Factorial of Number
        val factorial = ProblemEntity(
            id = 62,
            title = "Factorial of Number",
            description = """
                Given a non-negative integer n, calculate its factorial.
                
                Factorial of n (denoted as n!) is the product of all positive integers less than or equal to n.
                
                n! = n × (n-1) × (n-2) × ... × 1
                By definition, 0! = 1.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 0 <= n <= 12
                • The result fits in a 32-bit integer.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 5",
                    output = "120",
                    explanation = "5! = 5 × 4 × 3 × 2 × 1 = 120"
                ),
                Example(
                    input = "n = 0",
                    output = "1",
                    explanation = "0! = 1 by definition"
                )
            ),
            hints = listOf(
                "Use a loop from 1 to n to accumulate the product.",
                "Be careful with the base case n = 0."
            ),
            editorial = """
                ## Approach: Iterative Product
                
                Multiply all numbers from 1 to n.
                
                **Algorithm:**
                1. If n == 0, return 1
                2. Initialize result = 1
                3. For i from 1 to n:
                   - result = result * i
                4. Return result
                
                **Alternative:** Use recursion: n! = n × (n-1)!
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 92.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun factorial(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int factorial(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def factorial(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var factorial = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int factorial(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "5", expectedOutput = "120", isHidden = false),
                TestCase(input = "0", expectedOutput = "1", isHidden = false),
                TestCase(input = "3", expectedOutput = "6", isHidden = false),
                TestCase(input = "10", expectedOutput = "3628800", isHidden = true),
                TestCase(input = "12", expectedOutput = "479001600", isHidden = true)
            )
        )
        problemDao.insertProblem(factorial)

        // Problem 63: Power of Two
        val powerOfTwo = ProblemEntity(
            id = 63,
            title = "Power of Two",
            description = """
                Given an integer n, return true if it is a power of two. Otherwise, return false.
                
                An integer n is a power of two if there exists an integer x such that n == 2ˣ.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -2³¹ <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 1",
                    output = "true",
                    explanation = "2⁰ = 1"
                ),
                Example(
                    input = "n = 16",
                    output = "true",
                    explanation = "2⁴ = 16"
                ),
                Example(
                    input = "n = 3",
                    output = "false",
                    explanation = "3 is not a power of two"
                )
            ),
            hints = listOf(
                "A power of two in binary has exactly one '1' bit.",
                "Use bit manipulation: n & (n-1) clears the lowest set bit."
            ),
            editorial = """
                ## Approach 1: Bit Manipulation (Optimal)
                
                If n is a power of two, it has exactly one 1 bit: n & (n-1) == 0.
                
                **Algorithm:**
                1. If n <= 0, return false
                2. Return (n & (n-1)) == 0
                
                ## Approach 2: Loop Division
                
                Keep dividing by 2 until we get 1 or an odd number.
                
                **Complexity Analysis:**
                • Time complexity: O(1) for bit op, O(log n) for loop
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 88.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isPowerOfTwo(n: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPowerOfTwo(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPowerOfTwo(n: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {boolean}
 */
var isPowerOfTwo = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPowerOfTwo(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "1", expectedOutput = "true", isHidden = false),
                TestCase(input = "16", expectedOutput = "true", isHidden = false),
                TestCase(input = "3", expectedOutput = "false", isHidden = false),
                TestCase(input = "0", expectedOutput = "false", isHidden = true),
                TestCase(input = "-16", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(powerOfTwo)

        // Problem 64: Power of Three
        val powerOfThree = ProblemEntity(
            id = 64,
            title = "Power of Three",
            description = """
                Given an integer n, return true if it is a power of three. Otherwise, return false.
                
                An integer n is a power of three if there exists an integer x such that n == 3ˣ.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • -2³¹ <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 27",
                    output = "true",
                    explanation = "3³ = 27"
                ),
                Example(
                    input = "n = 9",
                    output = "true",
                    explanation = "3² = 9"
                ),
                Example(
                    input = "n = 0",
                    output = "false",
                    explanation = "0 is not a power of three"
                )
            ),
            hints = listOf(
                "Keep dividing by 3 as long as the number is divisible by 3.",
                "If you end up with 1, it's a power of three."
            ),
            editorial = """
                ## Approach: Loop Division
                
                While n is divisible by 3, divide by 3.
                
                **Algorithm:**
                1. If n <= 0, return false
                2. While n % 3 == 0:
                   - n = n / 3
                3. Return n == 1
                
                **Optimization:** Since 3¹⁹ is the largest power of 3 within 32-bit int range, we can check if 3¹⁹ % n == 0.
                
                **Complexity Analysis:**
                • Time complexity: O(log₃ n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 85.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isPowerOfThree(n: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPowerOfThree(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPowerOfThree(n: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {boolean}
 */
var isPowerOfThree = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPowerOfThree(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "27", expectedOutput = "true", isHidden = false),
                TestCase(input = "9", expectedOutput = "true", isHidden = false),
                TestCase(input = "0", expectedOutput = "false", isHidden = false),
                TestCase(input = "45", expectedOutput = "false", isHidden = true),
                TestCase(input = "1", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(powerOfThree)

        // Problem 65: Square Root (Integer)
        val sqrtNumber = ProblemEntity(
            id = 65,
            title = "Square Root (Integer)",
            description = """
                Given a non-negative integer x, return the integer part of its square root.
                
                In other words, find the largest integer y such that y² ≤ x.
                
                For example, sqrt(8) = 2 because 2² = 4 ≤ 8, but 3² = 9 > 8.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Math",
            constraints = """
                • 0 <= x <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "x = 4",
                    output = "2",
                    explanation = "2² = 4"
                ),
                Example(
                    input = "x = 8",
                    output = "2",
                    explanation = "2² = 4 ≤ 8, 3² = 9 > 8"
                ),
                Example(
                    input = "x = 0",
                    output = "0",
                    explanation = "0² = 0"
                )
            ),
            hints = listOf(
                "Use binary search to find the integer square root.",
                "For mid, if mid² ≤ x, move left pointer up; otherwise move right pointer down."
            ),
            editorial = """
                ## Approach: Binary Search
                
                Binary search for the largest integer whose square is ≤ x.
                
                **Algorithm:**
                1. If x == 0 or x == 1, return x
                2. Initialize left = 1, right = x / 2
                3. While left <= right:
                   - mid = left + (right - left) / 2
                   - square = mid * mid
                   - If square == x: return mid
                   - Else if square < x: left = mid + 1, result = mid
                   - Else: right = mid - 1
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(log x)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun mySqrt(x: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int mySqrt(int x) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def mySqrt(x: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} x
 * @return {number}
 */
var mySqrt = function(x) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int mySqrt(int x) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "4", expectedOutput = "2", isHidden = false),
                TestCase(input = "8", expectedOutput = "2", isHidden = false),
                TestCase(input = "0", expectedOutput = "0", isHidden = false),
                TestCase(input = "2147395600", expectedOutput = "46340", isHidden = true)
            )
        )
        problemDao.insertProblem(sqrtNumber)

        // Problem 66: Valid Perfect Square
        val perfectSquare = ProblemEntity(
            id = 66,
            title = "Valid Perfect Square",
            description = """
                Given a positive integer num, return true if num is a perfect square, false otherwise.
                
                A perfect square is an integer that is the square of an integer.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 1 <= num <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "num = 16",
                    output = "true",
                    explanation = "4 × 4 = 16"
                ),
                Example(
                    input = "num = 14",
                    output = "false",
                    explanation = "No integer squared equals 14"
                )
            ),
            hints = listOf(
                "Use binary search to find if there's an integer whose square equals num.",
                "Start with left = 1, right = num."
            ),
            editorial = """
                ## Approach: Binary Search
                
                Binary search for an integer whose square equals num.
                
                **Algorithm:**
                1. Initialize left = 1, right = num
                2. While left <= right:
                   - mid = left + (right - left) / 2
                   - square = mid * mid
                   - If square == num: return true
                   - Else if square < num: left = mid + 1
                   - Else: right = mid - 1
                3. Return false
                
                **Alternative:** Newton's method for faster convergence.
                
                **Complexity Analysis:**
                • Time complexity: O(log num)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 82.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun isPerfectSquare(num: Int): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPerfectSquare(int num) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPerfectSquare(num: int) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} num
 * @return {boolean}
 */
var isPerfectSquare = function(num) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPerfectSquare(int num) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "16", expectedOutput = "true", isHidden = false),
                TestCase(input = "14", expectedOutput = "false", isHidden = false),
                TestCase(input = "1", expectedOutput = "true", isHidden = false),
                TestCase(input = "2147483647", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(perfectSquare)

        // Problem 67: Add Binary Strings
        val addBinary = ProblemEntity(
            id = 67,
            title = "Add Binary Strings",
            description = """
                Given two binary strings a and b, return their sum as a binary string.
                
                Binary strings contain only '0' and '1' characters.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= a.length, b.length <= 10⁴
                • Strings contain only '0' or '1'
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "a = \"11\", b = \"1\"",
                    output = "\"100\"",
                    explanation = "11 + 1 = 100 in binary"
                ),
                Example(
                    input = "a = \"1010\", b = \"1011\"",
                    output = "\"10101\"",
                    explanation = "1010 + 1011 = 10101 in binary"
                )
            ),
            hints = listOf(
                "Process both strings from right to left.",
                "Keep track of carry.",
                "Similar to adding decimal numbers, but in base 2."
            ),
            editorial = """
                ## Approach: Manual Binary Addition
                
                Add digits from right to left with carry.
                
                **Algorithm:**
                1. Initialize result = StringBuilder()
                2. Initialize carry = 0, i = a.length - 1, j = b.length - 1
                3. While i >= 0 or j >= 0 or carry > 0:
                   - sum = carry
                   - If i >= 0: sum += a[i--] - '0'
                   - If j >= 0: sum += b[j--] - '0'
                   - result.append(sum % 2)
                   - carry = sum / 2
                4. Return result.reverse().toString()
                
                **Complexity Analysis:**
                • Time complexity: O(max(m, n))
                • Space complexity: O(max(m, n)) for result
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 78.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun addBinary(a: String, b: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String addBinary(String a, String b) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def addBinary(a: str, b: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} a
 * @param {string} b
 * @return {string}
 */
var addBinary = function(a, b) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string addBinary(string a, string b) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "11\n1", expectedOutput = "100", isHidden = false),
                TestCase(input = "1010\n1011", expectedOutput = "10101", isHidden = false),
                TestCase(input = "0\n0", expectedOutput = "0", isHidden = false),
                TestCase(input = "1111\n1111", expectedOutput = "11110", isHidden = true)
            )
        )
        problemDao.insertProblem(addBinary)

        // Problem 68: Hamming Distance
        val hammingDistance = ProblemEntity(
            id = 68,
            title = "Hamming Distance",
            description = """
                The Hamming distance between two integers is the number of positions at which the corresponding bits are different.
                
                Given two integers x and y, return the Hamming distance between them.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 0 <= x, y <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "x = 1, y = 4",
                    output = "2",
                    explanation = "1 (0 0 0 1), 4 (0 1 0 0). Bits differ at position 2 and 4 → 2 differences."
                ),
                Example(
                    input = "x = 3, y = 1",
                    output = "1",
                    explanation = "3 (0 1 1), 1 (0 0 1). Only one bit differs."
                )
            ),
            hints = listOf(
                "Use XOR to find bits that are different.",
                "Count the number of 1 bits in the XOR result."
            ),
            editorial = """
                ## Approach: XOR + Bit Counting
                
                XOR gives 1 where bits differ, then count ones.
                
                **Algorithm:**
                1. xor = x ^ y
                2. Count number of 1 bits in xor
                
                **Counting Bits:**
                - While xor > 0:
                  - xor = xor & (xor - 1)  // removes rightmost 1
                  - count++
                
                **Complexity Analysis:**
                • Time complexity: O(number of bits) = O(32)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun hammingDistance(x: Int, y: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int hammingDistance(int x, int y) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def hammingDistance(x: int, y: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} x
 * @param {number} y
 * @return {number}
 */
var hammingDistance = function(x, y) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int hammingDistance(int x, int y) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "1\n4", expectedOutput = "2", isHidden = false),
                TestCase(input = "3\n1", expectedOutput = "1", isHidden = false),
                TestCase(input = "0\n0", expectedOutput = "0", isHidden = false),
                TestCase(input = "123\n456", expectedOutput = "7", isHidden = true)
            )
        )
        problemDao.insertProblem(hammingDistance)

        // Problem 69: Count Set Bits
        val countBits = ProblemEntity(
            id = 69,
            title = "Count Set Bits",
            description = """
                Write a function that takes an unsigned integer and returns the number of '1' bits it has (also known as the Hamming weight).
                
                For example, the 32-bit integer '11' has binary representation 00000000000000000000000000001011, so it has three '1' bits.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Math",
            constraints = """
                • 0 <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 11",
                    output = "3",
                    explanation = "Binary: 1011 has three 1 bits"
                ),
                Example(
                    input = "n = 128",
                    output = "1",
                    explanation = "Binary: 10000000 has one 1 bit"
                )
            ),
            hints = listOf(
                "Use n & (n-1) to remove the lowest set bit.",
                "Count how many times you can do this until n becomes 0."
            ),
            editorial = """
                ## Approach 1: Brian Kernighan's Algorithm (Optimal)
                
                Repeatedly clear the lowest set bit and count.
                
                **Algorithm:**
                1. Initialize count = 0
                2. While n != 0:
                   - n = n & (n - 1)  // removes lowest set bit
                   - count++
                3. Return count
                
                ## Approach 2: Loop Over All Bits
                
                Check each of the 32 bits using bit shifting.
                
                **Complexity Analysis:**
                • Time complexity: O(number of set bits) for Kernighan, O(32) for loop
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 88.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun countSetBits(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countSetBits(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countSetBits(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number} n
 * @return {number}
 */
var countSetBits = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countSetBits(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "11", expectedOutput = "3", isHidden = false),
                TestCase(input = "128", expectedOutput = "1", isHidden = false),
                TestCase(input = "0", expectedOutput = "0", isHidden = false),
                TestCase(input = "2147483647", expectedOutput = "31", isHidden = true)
            )
        )
        problemDao.insertProblem(countBits)

        // Problem 70: Single Number
        val singleNumber = ProblemEntity(
            id = 70,
            title = "Single Number",
            description = """
                Given a non-empty array of integers, every element appears twice except for one. Find that single one.
                
                You must implement a solution with linear runtime complexity and use only constant extra space.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 3 × 10⁴
                • Each element appears twice except one
                • -3 × 10⁴ <= nums[i] <= 3 × 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [2, 2, 1]",
                    output = "1",
                    explanation = "1 appears once, all others twice"
                ),
                Example(
                    input = "nums = [4, 1, 2, 1, 2]",
                    output = "4",
                    explanation = "4 appears once, all others twice"
                )
            ),
            hints = listOf(
                "Use XOR operation. a XOR a = 0, a XOR 0 = a.",
                "XOR all numbers together - duplicates will cancel out."
            ),
            editorial = """
                ## Approach: XOR Bit Manipulation
                
                XOR all numbers together. Duplicates cancel out.
                
                **Algorithm:**
                1. Initialize result = 0
                2. For each num in nums:
                   - result = result XOR num
                3. Return result
                
                **Why it works:**
                - a XOR a = 0 (same numbers cancel)
                - a XOR 0 = a
                - XOR is commutative and associative
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 90.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun singleNumber(nums: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int singleNumber(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def singleNumber(nums: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number}
 */
var singleNumber = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int singleNumber(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[2, 2, 1]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[4, 1, 2, 1, 2]", expectedOutput = "4", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[1, 0, 1]", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(singleNumber)

        // ==================== MORE ADVANCED ALGORITHMS (71-86) ====================

        // Problem 71: Majority Element
        val majorityElement = ProblemEntity(
            id = 71,
            title = "Majority Element",
            description = """
                Given an array of size n, find the majority element. The majority element is the element that appears more than ⌊n/2⌋ times.
                
                You may assume that the array is non-empty and that a majority element always exists.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • n == nums.length
                • 1 <= n <= 5 × 10⁴
                • -10⁹ <= nums[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [3, 2, 3]",
                    output = "3",
                    explanation = "3 appears twice, which is more than ⌊3/2⌋ = 1"
                ),
                Example(
                    input = "nums = [2, 2, 1, 1, 1, 2, 2]",
                    output = "2",
                    explanation = "2 appears 4 times, more than ⌊7/2⌋ = 3"
                )
            ),
            hints = listOf(
                "Use Boyer-Moore Voting Algorithm for O(n) time and O(1) space.",
                "The algorithm works by cancelling out pairs of different elements."
            ),
            editorial = """
                ## Approach: Boyer-Moore Voting Algorithm
                
                Maintain a candidate and count. When count == 0, pick a new candidate.
                
                **Algorithm:**
                1. Initialize candidate = null, count = 0
                2. For each num in nums:
                   - If count == 0: candidate = num
                   - count += 1 if num == candidate else -1
                3. Return candidate (verification step may be needed)
                
                **Why it works:** Majority element will survive after canceling pairs.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 80.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun majorityElement(nums: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int majorityElement(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def majorityElement(nums: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number}
 */
var majorityElement = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int majorityElement(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[3, 2, 3]", expectedOutput = "3", isHidden = false),
                TestCase(input = "[2, 2, 1, 1, 1, 2, 2]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "1", isHidden = false),
                TestCase(input = "[6, 5, 5]", expectedOutput = "5", isHidden = true)
            )
        )
        problemDao.insertProblem(majorityElement)

        // Problem 72: Merge Sorted Arrays
        val mergeSortedArrays = ProblemEntity(
            id = 72,
            title = "Merge Sorted Arrays",
            description = """
                You are given two integer arrays nums1 and nums2, sorted in non-decreasing order.
                
                Merge nums2 into nums1 as one sorted array.
                
                Note: nums1 has enough space (size m + n) to hold additional elements from nums2.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • nums1.length == m + n
                • nums2.length == n
                • 0 <= m, n <= 200
                • -10⁹ <= nums1[i], nums2[j] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3",
                    output = "[1,2,2,3,5,6]",
                    explanation = "Merge sorted arrays into nums1"
                ),
                Example(
                    input = "nums1 = [1], m = 1, nums2 = [], n = 0",
                    output = "[1]",
                    explanation = "Second array is empty"
                )
            ),
            hints = listOf(
                "Start merging from the end of both arrays.",
                "Use three pointers: one for nums1 end, one for nums2 end, one for merged position."
            ),
            editorial = """
                ## Approach: Three Pointers from End
                
                Merge from the end to avoid overwriting elements in nums1.
                
                **Algorithm:**
                1. Initialize p1 = m - 1, p2 = n - 1, p = m + n - 1
                2. While p2 >= 0:
                   - If p1 >= 0 and nums1[p1] > nums2[p2]:
                     - nums1[p] = nums1[p1]
                     - p1--
                   - Else:
                     - nums1[p] = nums2[p2]
                     - p2--
                   - p--
                3. Return nums1
                
                **Complexity Analysis:**
                • Time complexity: O(m + n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun mergeSortedArrays(nums1: IntArray, m: Int, nums2: IntArray, n: Int): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] mergeSortedArrays(int[] nums1, int m, int[] nums2, int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def mergeSortedArrays(nums1: List[int], m: int, nums2: List[int], n: int) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums1
 * @param {number} m
 * @param {number[]} nums2
 * @param {number} n
 * @return {number[]}
 */
var mergeSortedArrays = function(nums1, m, nums2, n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> mergeSortedArrays(vector<int>& nums1, int m, vector<int>& nums2, int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1,2,3,0,0,0]\n3\n[2,5,6]\n3",
                    expectedOutput = "[1,2,2,3,5,6]",
                    isHidden = false
                ),
                TestCase(input = "[1]\n1\n[]\n0", expectedOutput = "[1]", isHidden = false),
                TestCase(input = "[0]\n0\n[1]\n1", expectedOutput = "[1]", isHidden = false),
                TestCase(input = "[2,0]\n1\n[1]\n1", expectedOutput = "[1,2]", isHidden = true)
            )
        )
        problemDao.insertProblem(mergeSortedArrays)

        // Problem 73: Binary Search
        val binarySearch = ProblemEntity(
            id = 73,
            title = "Binary Search",
            description = """
                Given an array of integers nums which is sorted in ascending order, and an integer target, write a function to search target in nums.
                
                If target exists, return its index. Otherwise, return -1.
                
                You must write an algorithm with O(log n) runtime complexity.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 10⁴
                • -10⁴ <= nums[i], target <= 10⁴
                • All integers in nums are unique
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [-1,0,3,5,9,12], target = 9",
                    output = "4",
                    explanation = "9 exists in nums at index 4"
                ),
                Example(
                    input = "nums = [-1,0,3,5,9,12], target = 2",
                    output = "-1",
                    explanation = "2 does not exist in nums"
                )
            ),
            hints = listOf(
                "Binary search works by repeatedly dividing the search space in half.",
                "Compare target with middle element to decide which half to search."
            ),
            editorial = """
                ## Approach: Iterative Binary Search
                
                Maintain left and right pointers, find mid, and narrow down search space.
                
                **Algorithm:**
                1. Initialize left = 0, right = nums.length - 1
                2. While left <= right:
                   - mid = left + (right - left) / 2
                   - If nums[mid] == target: return mid
                   - Else if nums[mid] < target: left = mid + 1
                   - Else: right = mid - 1
                3. Return -1
                
                **Note:** Use mid = left + (right - left) / 2 to avoid overflow.
                
                **Complexity Analysis:**
                • Time complexity: O(log n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 88.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun binarySearch(nums: IntArray, target: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int binarySearch(int[] nums, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def binarySearch(nums: List[int], target: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number}
 */
var binarySearch = function(nums, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int binarySearch(vector<int>& nums, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[-1,0,3,5,9,12]\n9", expectedOutput = "4", isHidden = false),
                TestCase(input = "[-1,0,3,5,9,12]\n2", expectedOutput = "-1", isHidden = false),
                TestCase(input = "[5]\n5", expectedOutput = "0", isHidden = false),
                TestCase(input = "[1,2,3,4,5]\n3", expectedOutput = "2", isHidden = true)
            )
        )
        problemDao.insertProblem(binarySearch)

        // Problem 74: First Bad Version
        val firstBadVersion = ProblemEntity(
            id = 74,
            title = "First Bad Version",
            description = """
                You are a product manager and currently leading a team to develop a new product. 
                Each version is based on the previous version, and all versions after a bad version are also bad.
                
                Given n versions [1, 2, ..., n], find the first bad version.
                
                You have an API isBadVersion(version) that returns whether a version is bad.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= bad <= n <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 5, bad = 4",
                    output = "4",
                    explanation = "Version 4 is the first bad version"
                ),
                Example(
                    input = "n = 1, bad = 1",
                    output = "1",
                    explanation = "Only one version"
                )
            ),
            hints = listOf(
                "Use binary search to find the boundary between good and bad versions.",
                "If a version is bad, the first bad version could be at or before it."
            ),
            editorial = """
                ## Approach: Binary Search for First Bad
                
                Find the leftmost version that is bad.
                
                **Algorithm:**
                1. Initialize left = 1, right = n
                2. While left < right:
                   - mid = left + (right - left) / 2
                   - If isBadVersion(mid):
                     - right = mid  // bad, could be first, search left
                   - Else:
                     - left = mid + 1  // good, search right
                3. Return left
                
                **Complexity Analysis:**
                • Time complexity: O(log n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 82.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
/* The API isBadVersion is defined for you.
 * fun isBadVersion(version: Int): Boolean {} */

fun firstBadVersion(n: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
/* The isBadVersion API is defined in the parent class.
   boolean isBadVersion(int version); */

public class Solution {
    public int firstBadVersion(int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
# The isBadVersion API is already defined for you.
# def isBadVersion(version: int) -> bool:

def firstBadVersion(n: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * Definition for isBadVersion()
 * 
 * @param {integer} version number
 * @return {boolean} whether the version is bad
 * var isBadVersion = function(version) {}
 */

var firstBadVersion = function(n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
// The API isBadVersion is defined for you.
// bool isBadVersion(int version);

class Solution {
public:
    int firstBadVersion(int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "5\n4", expectedOutput = "4", isHidden = false),
                TestCase(input = "1\n1", expectedOutput = "1", isHidden = false),
                TestCase(input = "10\n1", expectedOutput = "1", isHidden = true),
                TestCase(input = "100\n50", expectedOutput = "50", isHidden = true)
            )
        )
        problemDao.insertProblem(firstBadVersion)

        // Problem 75: Search Insert Position
        val searchInsert = ProblemEntity(
            id = 75,
            title = "Search Insert Position",
            description = """
                Given a sorted array of distinct integers and a target value, return the index if the target is found.
                If not, return the index where it would be inserted to maintain order.
                
                The algorithm should run in O(log n) time.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 10⁴
                • -10⁴ <= nums[i] <= 10⁴
                • All integers are unique
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [1,3,5,6], target = 5",
                    output = "2",
                    explanation = "Target found at index 2"
                ),
                Example(
                    input = "nums = [1,3,5,6], target = 2",
                    output = "1",
                    explanation = "2 would be inserted at index 1"
                ),
                Example(
                    input = "nums = [1,3,5,6], target = 7",
                    output = "4",
                    explanation = "7 would be inserted at the end"
                )
            ),
            hints = listOf(
                "Use binary search, and return left pointer when not found.",
                "The insertion position is where the target would go."
            ),
            editorial = """
                ## Approach: Binary Search Variant
                
                Binary search that returns left pointer when target not found.
                
                **Algorithm:**
                1. Initialize left = 0, right = nums.length
                2. While left < right:
                   - mid = left + (right - left) / 2
                   - If nums[mid] < target:
                     - left = mid + 1
                   - Else:
                     - right = mid
                3. Return left
                
                **Complexity Analysis:**
                • Time complexity: O(log n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 85.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun searchInsert(nums: IntArray, target: Int): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int searchInsert(int[] nums, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def searchInsert(nums: List[int], target: int) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number}
 */
var searchInsert = function(nums, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int searchInsert(vector<int>& nums, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1,3,5,6]\n5", expectedOutput = "2", isHidden = false),
                TestCase(input = "[1,3,5,6]\n2", expectedOutput = "1", isHidden = false),
                TestCase(input = "[1,3,5,6]\n7", expectedOutput = "4", isHidden = false),
                TestCase(input = "[1]\n0", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(searchInsert)

        // Problem 76: Find Peak Element
        val peakElement = ProblemEntity(
            id = 76,
            title = "Find Peak Element",
            description = """
                A peak element is an element that is strictly greater than its neighbors.
                
                Given a 0-indexed integer array nums, find a peak element and return its index.
                
                You may imagine that nums[-1] = nums[n] = -∞ (negative infinity).
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 1000
                • -2³¹ <= nums[i] <= 2³¹ - 1
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [1,2,3,1]",
                    output = "2",
                    explanation = "3 is a peak element at index 2"
                ),
                Example(
                    input = "nums = [1,2,1,3,5,6,4]",
                    output = "5",
                    explanation = "6 is a peak element at index 5"
                )
            ),
            hints = listOf(
                "Use binary search - move towards the side with a larger neighbor.",
                "If nums[mid] < nums[mid+1], peak is on the right side."
            ),
            editorial = """
                ## Approach: Binary Search on Array
                
                Look for a point where the element is greater than its neighbors.
                
                **Algorithm:**
                1. Initialize left = 0, right = nums.length - 1
                2. While left < right:
                   - mid = left + (right - left) / 2
                   - If nums[mid] < nums[mid + 1]:
                     - left = mid + 1  // peak is on the right
                   - Else:
                     - right = mid  // peak is at or before mid
                3. Return left
                
                **Complexity Analysis:**
                • Time complexity: O(log n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun findPeakElement(nums: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int findPeakElement(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def findPeakElement(nums: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number}
 */
var findPeakElement = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int findPeakElement(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1,2,3,1]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[1,2,1,3,5,6,4]", expectedOutput = "5", isHidden = false),
                TestCase(input = "[1]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[3,2,1]", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(peakElement)

        // Problem 77: Sort Colors (Dutch Flag)
        val sortColors = ProblemEntity(
            id = 77,
            title = "Sort Colors (Dutch Flag)",
            description = """
                Given an array nums with n objects colored red, white, or blue, sort them in-place so that objects of the same color are adjacent.
                
                Use integers 0, 1, and 2 to represent red, white, and blue respectively.
                The Dutch National Flag problem requires sorting without using a sort library, in O(n) time.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • n == nums.length
                • 1 <= n <= 300
                • nums[i] is 0, 1, or 2
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [2,0,2,1,1,0]",
                    output = "[0,0,1,1,2,2]",
                    explanation = "Colors sorted: reds first, then whites, then blues"
                ),
                Example(
                    input = "nums = [2,0,1]",
                    output = "[0,1,2]",
                    explanation = "Sorted colors"
                )
            ),
            hints = listOf(
                "Use three pointers: low for 0s, mid for 1s, high for 2s.",
                "Swap elements to their correct sections."
            ),
            editorial = """
                ## Approach: Three Pointers (Dutch Flag Algorithm)
                
                Partition the array into three sections: 0s, 1s, and 2s.
                
                **Algorithm:**
                1. Initialize low = 0, mid = 0, high = nums.length - 1
                2. While mid <= high:
                   - If nums[mid] == 0:
                     - Swap nums[low] and nums[mid]
                     - low++, mid++
                   - Else if nums[mid] == 1:
                     - mid++
                   - Else:  // nums[mid] == 2
                     - Swap nums[mid] and nums[high]
                     - high--
                3. Return sorted array
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 72.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun sortColors(nums: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] sortColors(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def sortColors(nums: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number[]}
 */
var sortColors = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> sortColors(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[2,0,2,1,1,0]",
                    expectedOutput = "[0,0,1,1,2,2]",
                    isHidden = false
                ),
                TestCase(input = "[2,0,1]", expectedOutput = "[0,1,2]", isHidden = false),
                TestCase(input = "[0]", expectedOutput = "[0]", isHidden = false),
                TestCase(
                    input = "[2,2,2,1,1,0,0]",
                    expectedOutput = "[0,0,1,1,2,2,2]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(sortColors)

        // Problem 78: Three Sum (Find Triplets)
        val threeSum = ProblemEntity(
            id = 78,
            title = "Three Sum (Find Triplets)",
            description = """
                Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that:
                - i != j, i != k, and j != k
                - nums[i] + nums[j] + nums[k] == 0
                
                The solution set must not contain duplicate triplets.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 3 <= nums.length <= 3000
                • -10⁵ <= nums[i] <= 10⁵
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [-1,0,1,2,-1,-4]",
                    output = "[[-1,-1,2],[-1,0,1]]",
                    explanation = "Two unique triplets sum to zero"
                ),
                Example(
                    input = "nums = [0,1,1]",
                    output = "[]",
                    explanation = "No triplets sum to zero"
                )
            ),
            hints = listOf(
                "Sort the array first to avoid duplicates and use two pointers.",
                "Fix one element, then use two pointers to find two others that sum to -fixed."
            ),
            editorial = """
                ## Approach: Sorting + Two Pointers
                
                Sort array, fix first element, then use two pointers for remaining.
                
                **Algorithm:**
                1. Sort the array
                2. Initialize result list
                3. For i from 0 to n-3:
                   - Skip duplicates: if i > 0 and nums[i] == nums[i-1], continue
                   - left = i + 1, right = n - 1
                   - While left < right:
                     - sum = nums[i] + nums[left] + nums[right]
                     - If sum == 0:
                       - Add [nums[i], nums[left], nums[right]] to result
                       - Skip duplicates for left and right
                       - left++, right--
                     - Else if sum < 0: left++
                     - Else: right--
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n²)
                • Space complexity: O(1) or O(n) for output
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 68.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun threeSum(nums: IntArray): List<List<Int>> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def threeSum(nums: List[int]) -> List[List[int]]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number[][]}
 */
var threeSum = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<vector<int>> threeSum(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[-1,0,1,2,-1,-4]",
                    expectedOutput = "[[-1,-1,2],[-1,0,1]]",
                    isHidden = false
                ),
                TestCase(input = "[0,1,1]", expectedOutput = "[]", isHidden = false),
                TestCase(input = "[0,0,0]", expectedOutput = "[[0,0,0]]", isHidden = false),
                TestCase(
                    input = "[-2,0,1,1,2]",
                    expectedOutput = "[[-2,0,2],[-2,1,1]]",
                    isHidden = true
                )
            )
        )
        problemDao.insertProblem(threeSum)

        // Problem 79: Four Sum
        val fourSum = ProblemEntity(
            id = 79,
            title = "Four Sum",
            description = """
                Given an array nums of n integers, return an array of all unique quadruplets [nums[a], nums[b], nums[c], nums[d]] such that:
                - a, b, c, d are distinct indices
                - nums[a] + nums[b] + nums[c] + nums[d] == target
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 200
                • -10⁹ <= nums[i] <= 10⁹
                • -10⁹ <= target <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [1,0,-1,0,-2,2], target = 0",
                    output = "[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]",
                    explanation = "Unique quadruplets summing to 0"
                ),
                Example(
                    input = "nums = [2,2,2,2,2], target = 8",
                    output = "[[2,2,2,2]]",
                    explanation = "One quadruplet"
                )
            ),
            hints = listOf(
                "Similar to 3Sum, but with two nested loops and two pointers.",
                "Sort the array and skip duplicates to avoid repeated quadruplets."
            ),
            editorial = """
                ## Approach: Sorting + Two Pointers (Nested)
                
                Fix two elements, then use two pointers for remaining two.
                
                **Algorithm:**
                1. Sort the array
                2. Initialize result list
                3. For i from 0 to n-4:
                   - Skip duplicates for i
                   - For j from i+1 to n-3:
                     - Skip duplicates for j
                     - left = j + 1, right = n - 1
                     - While left < right:
                       - sum = nums[i] + nums[j] + nums[left] + nums[right]
                       - If sum == target:
                         - Add quadruplet to result
                         - Skip duplicates for left and right
                         - left++, right--
                       - Else if sum < target: left++
                       - Else: right--
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n³)
                • Space complexity: O(1) or O(n) for output
            """.trimIndent(),
            timeEstimateMinutes = 35,
            acceptanceRate = 65.0f,
            xpReward = 35,
            starterCode = mapOf(
                "Kotlin" to """
fun fourSum(nums: IntArray, target: Int): List<List<Int>> {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def fourSum(nums: List[int], target: int) -> List[List[int]]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @param {number} target
 * @return {number[][]}
 */
var fourSum = function(nums, target) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<vector<int>> fourSum(vector<int>& nums, int target) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1,0,-1,0,-2,2]\n0",
                    expectedOutput = "[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]",
                    isHidden = false
                ),
                TestCase(
                    input = "[2,2,2,2,2]\n8",
                    expectedOutput = "[[2,2,2,2]]",
                    isHidden = false
                ),
                TestCase(input = "[0,0,0,0]\n0", expectedOutput = "[[0,0,0,0]]", isHidden = false),
                TestCase(input = "[1,1,1,1,1]\n4", expectedOutput = "[[1,1,1,1]]", isHidden = true)
            )
        )
        problemDao.insertProblem(fourSum)

        // Problem 80: Longest Consecutive Sequence
        val longestConsecutive = ProblemEntity(
            id = 80,
            title = "Longest Consecutive Sequence",
            description = """
                Given an unsorted array of integers, find the length of the longest consecutive elements sequence.
                
                Your algorithm must run in O(n) time.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 0 <= nums.length <= 10⁵
                • -10⁹ <= nums[i] <= 10⁹
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [100,4,200,1,3,2]",
                    output = "4",
                    explanation = "The longest consecutive sequence is [1,2,3,4] of length 4"
                ),
                Example(
                    input = "nums = [0,3,7,2,5,8,4,6,0,1]",
                    output = "9",
                    explanation = "Longest consecutive sequence from 0 to 8"
                )
            ),
            hints = listOf(
                "Use a HashSet to allow O(1) lookups.",
                "Only start counting from numbers that don't have a predecessor (num-1 not in set)."
            ),
            editorial = """
                ## Approach: HashSet with Sequence Detection
                
                Use set for O(1) lookups, and only build sequences from potential starts.
                
                **Algorithm:**
                1. Add all numbers to a HashSet
                2. Initialize longestStreak = 0
                3. For each num in set:
                   - If num - 1 not in set (potential start):
                     - currentNum = num
                     - currentStreak = 1
                     - While currentNum + 1 in set:
                       - currentNum++, currentStreak++
                     - longestStreak = max(longestStreak, currentStreak)
                4. Return longestStreak
                
                **Complexity Analysis:**
                • Time complexity: O(n) - each number processed at most twice
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 70.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun longestConsecutive(nums: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int longestConsecutive(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def longestConsecutive(nums: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number}
 */
var longestConsecutive = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int longestConsecutive(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[100,4,200,1,3,2]", expectedOutput = "4", isHidden = false),
                TestCase(input = "[0,3,7,2,5,8,4,6,0,1]", expectedOutput = "9", isHidden = false),
                TestCase(input = "[]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[1,2,0,1]", expectedOutput = "3", isHidden = true)
            )
        )
        problemDao.insertProblem(longestConsecutive)

        // Problem 81: Next Permutation
        val nextPermutation = ProblemEntity(
            id = 81,
            title = "Next Permutation",
            description = """
                Given an array of integers, find the next lexicographically greater permutation.
                
                If such arrangement is not possible, rearrange it as the lowest possible order (sorted ascending).
                
                The transformation must be done in-place.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 100
                • 0 <= nums[i] <= 100
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [1,2,3]",
                    output = "[1,3,2]",
                    explanation = "Next permutation after [1,2,3]"
                ),
                Example(
                    input = "nums = [3,2,1]",
                    output = "[1,2,3]",
                    explanation = "No next permutation, so sort ascending"
                ),
                Example(
                    input = "nums = [1,1,5]",
                    output = "[1,5,1]",
                    explanation = "Next permutation"
                )
            ),
            hints = listOf(
                "Find the first decreasing element from the right.",
                "Swap it with the next larger element to its right, then reverse the suffix."
            ),
            editorial = """
                ## Approach: Single Pass Algorithm
                
                Find pivot, swap with successor, then reverse suffix.
                
                **Algorithm:**
                1. Find first index i from right where nums[i] < nums[i+1] (pivot)
                2. If no such index, reverse entire array and return
                3. Find index j from right where nums[j] > nums[i] (successor)
                4. Swap nums[i] and nums[j]
                5. Reverse the suffix from i+1 to end
                6. Return modified array
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 35,
            acceptanceRate = 68.0f,
            xpReward = 35,
            starterCode = mapOf(
                "Kotlin" to """
fun nextPermutation(nums: IntArray): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] nextPermutation(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def nextPermutation(nums: List[int]) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number[]}
 */
var nextPermutation = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> nextPermutation(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[1,2,3]", expectedOutput = "[1,3,2]", isHidden = false),
                TestCase(input = "[3,2,1]", expectedOutput = "[1,2,3]", isHidden = false),
                TestCase(input = "[1,1,5]", expectedOutput = "[1,5,1]", isHidden = false),
                TestCase(input = "[1,3,2]", expectedOutput = "[2,1,3]", isHidden = true)
            )
        )
        problemDao.insertProblem(nextPermutation)

        // Problem 82: Jump Game
        val jumpGame = ProblemEntity(
            id = 82,
            title = "Jump Game",
            description = """
                You are given an integer array nums. You are initially positioned at the first index.
                
                Each element in the array represents your maximum jump length at that position.
                
                Return true if you can reach the last index, or false otherwise.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 10⁴
                • 0 <= nums[i] <= 10⁵
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [2,3,1,1,4]",
                    output = "true",
                    explanation = "Jump 1 step to index 1, then 3 steps to the last index"
                ),
                Example(
                    input = "nums = [3,2,1,0,4]",
                    output = "false",
                    explanation = "You cannot reach index 4"
                )
            ),
            hints = listOf(
                "Track the farthest reachable index as you go.",
                "If at any point you're at an index beyond the farthest reachable, return false."
            ),
            editorial = """
                ## Approach: Greedy (Max Reachable)
                
                Track the maximum index that can be reached.
                
                **Algorithm:**
                1. Initialize maxReach = 0
                2. For i from 0 to n-1:
                   - If i > maxReach: return false
                   - maxReach = max(maxReach, i + nums[i])
                   - If maxReach >= n - 1: return true
                3. Return true
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 25,
            acceptanceRate = 75.0f,
            xpReward = 25,
            starterCode = mapOf(
                "Kotlin" to """
fun canJump(nums: IntArray): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean canJump(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def canJump(nums: List[int]) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {boolean}
 */
var canJump = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool canJump(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[2,3,1,1,4]", expectedOutput = "true", isHidden = false),
                TestCase(input = "[3,2,1,0,4]", expectedOutput = "false", isHidden = false),
                TestCase(input = "[0]", expectedOutput = "true", isHidden = false),
                TestCase(input = "[2,0,0]", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(jumpGame)

        // Problem 83: Jump Game II
        val jumpGameII = ProblemEntity(
            id = 83,
            title = "Jump Game II",
            description = """
                You are given a 0-indexed array of integers nums of length n. You are initially positioned at nums[0].
                
                Each element nums[i] represents the maximum length of a forward jump from index i.
                
                Return the minimum number of jumps to reach nums[n - 1].
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 10⁴
                • 0 <= nums[i] <= 1000
                • You can always reach the last index
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [2,3,1,1,4]",
                    output = "2",
                    explanation = "Minimum jumps: jump to index 1, then to last"
                ),
                Example(
                    input = "nums = [2,3,0,1,4]",
                    output = "2",
                    explanation = "Minimum jumps: 2 jumps"
                )
            ),
            hints = listOf(
                "Use BFS-like approach with greedy: track current and next reachable ranges.",
                "Increment jumps when you reach the end of current range."
            ),
            editorial = """
                ## Approach: Greedy BFS
                
                Track current range end and farthest reachable position.
                
                **Algorithm:**
                1. Initialize jumps = 0, currentEnd = 0, farthest = 0
                2. For i from 0 to n-2:
                   - farthest = max(farthest, i + nums[i])
                   - If i == currentEnd:
                     - jumps++
                     - currentEnd = farthest
                3. Return jumps
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 70.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun jump(nums: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int jump(int[] nums) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def jump(nums: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @return {number}
 */
var jump = function(nums) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int jump(vector<int>& nums) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "[2,3,1,1,4]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[2,3,0,1,4]", expectedOutput = "2", isHidden = false),
                TestCase(input = "[0]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[1,2,1,1,1]", expectedOutput = "3", isHidden = true)
            )
        )
        problemDao.insertProblem(jumpGameII)

        // Problem 84: Gas Station
        val gasStation = ProblemEntity(
            id = 84,
            title = "Gas Station",
            description = """
                There are n gas stations along a circular route. You have two integer arrays:
                - gas[i] is the amount of gas at station i
                - cost[i] is the amount of gas needed to travel from station i to i+1
                
                Return the starting gas station's index if you can travel around the circuit once, otherwise return -1.
            """.trimIndent(),
            difficulty = "Medium",
            topic = "Array",
            constraints = """
                • n == gas.length == cost.length
                • 1 <= n <= 10⁵
                • 0 <= gas[i], cost[i] <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "gas = [1,2,3,4,5], cost = [3,4,5,1,2]",
                    output = "3",
                    explanation = "Start at station 3 (index 3) and complete the circuit"
                ),
                Example(
                    input = "gas = [2,3,4], cost = [3,4,3]",
                    output = "-1",
                    explanation = "Cannot complete the circuit"
                )
            ),
            hints = listOf(
                "If total gas < total cost, impossible → return -1.",
                "Use a greedy approach: track current tank and start position."
            ),
            editorial = """
                ## Approach: Greedy Single Pass
                
                Keep track of total gas and current tank. Reset start when tank goes negative.
                
                **Algorithm:**
                1. Initialize totalGas = 0, currentGas = 0, start = 0
                2. For i from 0 to n-1:
                   - totalGas += gas[i] - cost[i]
                   - currentGas += gas[i] - cost[i]
                   - If currentGas < 0:
                     - start = i + 1
                     - currentGas = 0
                3. Return start if totalGas >= 0 else -1
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 30,
            acceptanceRate = 68.0f,
            xpReward = 30,
            starterCode = mapOf(
                "Kotlin" to """
fun canCompleteCircuit(gas: IntArray, cost: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int canCompleteCircuit(int[] gas, int[] cost) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def canCompleteCircuit(gas: List[int], cost: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} gas
 * @param {number[]} cost
 * @return {number}
 */
var canCompleteCircuit = function(gas, cost) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int canCompleteCircuit(vector<int>& gas, vector<int>& cost) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1,2,3,4,5]\n[3,4,5,1,2]",
                    expectedOutput = "3",
                    isHidden = false
                ),
                TestCase(input = "[2,3,4]\n[3,4,3]", expectedOutput = "-1", isHidden = false),
                TestCase(input = "[5]\n[4]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[5,1,2,3,4]\n[4,4,1,5,1]", expectedOutput = "4", isHidden = true)
            )
        )
        problemDao.insertProblem(gasStation)

        // Problem 85: Trapping Rain Water
        val trappingRainWater = ProblemEntity(
            id = 85,
            title = "Trapping Rain Water",
            description = """
                Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
                
                This is a classic hard problem that tests understanding of two-pointer technique.
            """.trimIndent(),
            difficulty = "Hard",
            topic = "Array",
            constraints = """
                • n == height.length
                • 1 <= n <= 2 × 10⁴
                • 0 <= height[i] <= 10⁵
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "height = [0,1,0,2,1,0,1,3,2,1,2,1]",
                    output = "6",
                    explanation = "6 units of rainwater are trapped"
                ),
                Example(
                    input = "height = [4,2,0,3,2,5]",
                    output = "9",
                    explanation = "9 units of water trapped"
                )
            ),
            hints = listOf(
                "Use two pointers: one at left, one at right.",
                "Track the maximum height seen from left and right."
            ),
            editorial = """
                ## Approach: Two Pointers
                
                Track leftMax and rightMax, and move the smaller pointer inward.
                
                **Algorithm:**
                1. Initialize left = 0, right = n - 1
                2. Initialize leftMax = 0, rightMax = 0, water = 0
                3. While left < right:
                   - If height[left] < height[right]:
                     - If height[left] >= leftMax:
                       - leftMax = height[left]
                     - Else:
                       - water += leftMax - height[left]
                     - left++
                   - Else:
                     - If height[right] >= rightMax:
                       - rightMax = height[right]
                     - Else:
                       - water += rightMax - height[right]
                     - right--
                4. Return water
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 40,
            acceptanceRate = 65.0f,
            xpReward = 40,
            starterCode = mapOf(
                "Kotlin" to """
fun trap(height: IntArray): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int trap(int[] height) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def trap(height: List[int]) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} height
 * @return {number}
 */
var trap = function(height) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int trap(vector<int>& height) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[0,1,0,2,1,0,1,3,2,1,2,1]",
                    expectedOutput = "6",
                    isHidden = false
                ),
                TestCase(input = "[4,2,0,3,2,5]", expectedOutput = "9", isHidden = false),
                TestCase(input = "[0,0,0]", expectedOutput = "0", isHidden = false),
                TestCase(input = "[5,4,1,2]", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(trappingRainWater)

        // Problem 86: Sliding Window Maximum
        val slidingWindowMaximum = ProblemEntity(
            id = 86,
            title = "Sliding Window Maximum",
            description = """
                You are given an array of integers nums, and a sliding window of size k moving from left to right.
                
                For each window, find the maximum element. Return an array containing the maximums.
                
                This is a classic deque problem (monotonic queue).
            """.trimIndent(),
            difficulty = "Hard",
            topic = "Array",
            constraints = """
                • 1 <= nums.length <= 10⁵
                • 1 <= k <= nums.length
                • -10⁴ <= nums[i] <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "nums = [1,3,-1,-3,5,3,6,7], k = 3",
                    output = "[3,3,5,5,6,7]",
                    explanation = "Maximums of each sliding window of size 3"
                ),
                Example(
                    input = "nums = [1], k = 1",
                    output = "[1]",
                    explanation = "Single element"
                )
            ),
            hints = listOf(
                "Use a deque to maintain indices of potential maximums in decreasing order.",
                "Remove elements outside the current window from the front."
            ),
            editorial = """
                ## Approach: Monotonic Deque
                
                Maintain a deque that stores indices in decreasing order of values.
                
                **Algorithm:**
                1. Initialize deque and result list
                2. For i from 0 to n-1:
                   - Remove indices from front that are outside window (i - k)
                   - Remove from back while nums[back] < nums[i] (maintain decreasing order)
                   - Add current index i to back
                   - If i >= k - 1: add nums[deque.front()] to result
                3. Return result array
                
                **Complexity Analysis:**
                • Time complexity: O(n) - each element processed at most twice
                • Space complexity: O(k) - for the deque
            """.trimIndent(),
            timeEstimateMinutes = 40,
            acceptanceRate = 60.0f,
            xpReward = 45,
            starterCode = mapOf(
                "Kotlin" to """
fun maxSlidingWindow(nums: IntArray, k: Int): IntArray {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def maxSlidingWindow(nums: List[int], k: int) -> List[int]:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {number[]} nums
 * @param {number} k
 * @return {number[]}
 */
var maxSlidingWindow = function(nums, k) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(
                    input = "[1,3,-1,-3,5,3,6,7]\n3",
                    expectedOutput = "[3,3,5,5,6,7]",
                    isHidden = false
                ),
                TestCase(input = "[1]\n1", expectedOutput = "[1]", isHidden = false),
                TestCase(input = "[1,-1]\n1", expectedOutput = "[1,-1]", isHidden = false),
                TestCase(input = "[7,2,4]\n2", expectedOutput = "[7,4]", isHidden = true)
            )
        )
        problemDao.insertProblem(slidingWindowMaximum)

                // ==================== MORE STRING OPERATIONS (87-96) ====================

        // Problem 87: Check if Two Strings are Anagrams
        val anagramCheck = ProblemEntity(
            id = 87,
            title = "Check if Two Strings are Anagrams",
            description = """
                Given two strings s and t, return true if t is an anagram of s, and false otherwise.
                
                An anagram is a word or phrase formed by rearranging the letters of a different word or phrase, 
                typically using all the original letters exactly once.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length, t.length <= 5 × 10⁴
                • s and t consist of lowercase English letters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"anagram\", t = \"nagaram\"",
                    output = "true",
                    explanation = "Both strings contain same characters: a:3, n:1, g:1, r:1, m:1"
                ),
                Example(
                    input = "s = \"rat\", t = \"car\"",
                    output = "false",
                    explanation = "Different character frequencies"
                )
            ),
            hints = listOf(
                "Count frequency of each character in both strings.",
                "Compare if both frequency maps are equal."
            ),
            editorial = """
                ## Approach: Frequency Count
                
                Use an array of size 26 to count character frequencies.
                
                **Algorithm:**
                1. If lengths are different, return false
                2. Create array count of size 26 initialized to 0
                3. For each character in s: count[char - 'a']++
                4. For each character in t: count[char - 'a']--
                5. If all counts are 0, return true, else false
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) - fixed size array
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 86.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isAnagram(s: String, t: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isAnagram(String s, String t) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isAnagram(s: str, t: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {string} t
 * @return {boolean}
 */
var isAnagram = function(s, t) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isAnagram(string s, string t) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "anagram\nnagaram", expectedOutput = "true", isHidden = false),
                TestCase(input = "rat\ncar", expectedOutput = "false", isHidden = false),
                TestCase(input = "ab\nba", expectedOutput = "true", isHidden = false),
                TestCase(input = "a\nab", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(anagramCheck)

        // Problem 88: First Letter to Uppercase
        val capitalizeFirst = ProblemEntity(
            id = 88,
            title = "Capitalize First Letter of Each Word",
            description = """
                Given a string containing words separated by spaces, capitalize the first letter of each word.
                
            For example, "hello world" becomes "Hello World".
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 10⁴
                • s consists of lowercase letters and spaces only
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "\"Hello World\"",
                    explanation = "First letter of each word is capitalized"
                ),
                Example(
                    input = "s = \"the quick brown fox\"",
                    output = "\"The Quick Brown Fox\"",
                    explanation = "All words capitalized"
                )
            ),
            hints = listOf(
                "Split the string by spaces, capitalize each word, then join back.",
                "Remember to handle the first character of the first word specially."
            ),
            editorial = """
                ## Approach: Split and Capitalize
                
                Split string into words, capitalize first letter of each word.
                
                **Algorithm:**
                1. Split s by spaces into words array
                2. For each word:
                   - Convert first char to uppercase
                   - Keep rest of word as is
                   - Join with spaces
                3. Return the result
                
                **Alternative:** Process character by character, capitalizing after spaces.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 89.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun capitalizeWords(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String capitalizeWords(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def capitalizeWords(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var capitalizeWords = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string capitalizeWords(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "Hello World", isHidden = false),
                TestCase(input = "the quick brown fox", expectedOutput = "The Quick Brown Fox", isHidden = false),
                TestCase(input = "hello", expectedOutput = "Hello", isHidden = false),
                TestCase(input = "a b c", expectedOutput = "A B C", isHidden = true)
            )
        )
        problemDao.insertProblem(capitalizeFirst)

        // Problem 89: Check if String Contains Only Digits
        val isNumeric = ProblemEntity(
            id = 89,
            title = "Check if String Contains Only Digits",
            description = """
                Given a string s, return true if the string contains only digits (0-9), false otherwise.
                
                An empty string should return false.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"12345\"",
                    output = "true",
                    explanation = "All characters are digits"
                ),
                Example(
                    input = "s = \"123a45\"",
                    output = "false",
                    explanation = "Contains letter 'a'"
                ),
                Example(
                    input = "s = \"\"",
                    output = "false",
                    explanation = "Empty string has no digits"
                )
            ),
            hints = listOf(
                "Iterate through each character and check if it's a digit.",
                "Use Character.isDigit() method in most languages."
            ),
            editorial = """
                ## Approach: Character Check
                
                Check each character to ensure it's a digit.
                
                **Algorithm:**
                1. If string is empty, return false
                2. For each char in s:
                   - If char is not a digit, return false
                3. Return true
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 95.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun isNumeric(s: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isNumeric(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isNumeric(s: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {boolean}
 */
var isNumeric = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isNumeric(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "12345", expectedOutput = "true", isHidden = false),
                TestCase(input = "123a45", expectedOutput = "false", isHidden = false),
                TestCase(input = "", expectedOutput = "false", isHidden = false),
                TestCase(input = "0", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(isNumeric)

        // Problem 90: Replace All Spaces with Underscore
        val replaceSpaces = ProblemEntity(
            id = 90,
            title = "Replace Spaces with Underscore",
            description = """
                Given a string s, replace all space characters ' ' with underscores '_'.
                
                Return the modified string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "\"hello_world\"",
                    explanation = "Space replaced with underscore"
                ),
                Example(
                    input = "s = \"a b c\"",
                    output = "\"a_b_c\"",
                    explanation = "All spaces replaced"
                )
            ),
            hints = listOf(
                "Use string replace function: replace(' ', '_')",
                "Or iterate and build a new string."
            ),
            editorial = """
                ## Approach: Replace or Build
                
                Use built-in replace function or manually build result.
                
                **Algorithm:**
                1. Use s.replace(' ', '_') for simplicity
                2. Or iterate through characters:
                   - If char is space, append '_'
                   - Else append char
                3. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 98.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun replaceSpaces(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String replaceSpaces(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def replaceSpaces(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var replaceSpaces = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string replaceSpaces(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "hello_world", isHidden = false),
                TestCase(input = "a b c", expectedOutput = "a_b_c", isHidden = false),
                TestCase(input = "no_spaces", expectedOutput = "no_spaces", isHidden = false),
                TestCase(input = "   ", expectedOutput = "___", isHidden = true)
            )
        )
        problemDao.insertProblem(replaceSpaces)

        // Problem 91: Concatenate Two Strings
        val concatenateStrings = ProblemEntity(
            id = 91,
            title = "Concatenate Two Strings",
            description = """
                Given two strings s1 and s2, return their concatenation (s1 + s2).
                
                For example, "Hello" and "World" becomes "HelloWorld".
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s1.length, s2.length <= 10⁴
                • s1 and s2 consist of printable ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s1 = \"Hello\", s2 = \"World\"",
                    output = "\"HelloWorld\"",
                    explanation = "Strings joined together"
                ),
                Example(
                    input = "s1 = \"\", s2 = \"Test\"",
                    output = "\"Test\"",
                    explanation = "Empty string plus test"
                )
            ),
            hints = listOf(
                "Simply use the + operator or concat method.",
                "For better performance, use StringBuilder for multiple concatenations."
            ),
            editorial = """
                ## Approach: String Concatenation
                
                Return s1 + s2.
                
                **Algorithm:**
                1. Return s1 + s2
                
                **Note:** For multiple concatenations, use StringBuilder for efficiency.
                
                **Complexity Analysis:**
                • Time complexity: O(m + n)
                • Space complexity: O(m + n)
            """.trimIndent(),
            timeEstimateMinutes = 5,
            acceptanceRate = 99.0f,
            xpReward = 5,
            starterCode = mapOf(
                "Kotlin" to """
fun concatenateStrings(s1: String, s2: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String concatenateStrings(String s1, String s2) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def concatenateStrings(s1: str, s2: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s1
 * @param {string} s2
 * @return {string}
 */
var concatenateStrings = function(s1, s2) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string concatenateStrings(string s1, string s2) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "Hello\nWorld", expectedOutput = "HelloWorld", isHidden = false),
                TestCase(input = "\nTest", expectedOutput = "Test", isHidden = false),
                TestCase(input = "Hi\nThere", expectedOutput = "HiThere", isHidden = false),
                TestCase(input = "a\nb", expectedOutput = "ab", isHidden = true)
            )
        )
        problemDao.insertProblem(concatenateStrings)

        // Problem 92: Remove First and Last Character
        val trimFirstLast = ProblemEntity(
            id = 92,
            title = "Remove First and Last Character",
            description = """
                Given a string s, return a new string with the first and last characters removed.
                
                If the string has length less than 2, return an empty string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"Hello\"",
                    output = "\"ell\"",
                    explanation = "Remove 'H' and 'o'"
                ),
                Example(
                    input = "s = \"ab\"",
                    output = "\"\"",
                    explanation = "After removing both characters, empty string remains"
                )
            ),
            hints = listOf(
                "Use substring with indices 1 to length-1.",
                "Check if length > 2 before trimming."
            ),
            editorial = """
                ## Approach: Substring
                
                Extract substring from index 1 to length-2.
                
                **Algorithm:**
                1. If s.length < 2, return ""
                2. Return s.substring(1, s.length - 1)
                
                **Complexity Analysis:**
                • Time complexity: O(n) for substring copy
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 94.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun trimFirstLast(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String trimFirstLast(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def trimFirstLast(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var trimFirstLast = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string trimFirstLast(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "Hello", expectedOutput = "ell", isHidden = false),
                TestCase(input = "ab", expectedOutput = "", isHidden = false),
                TestCase(input = "a", expectedOutput = "", isHidden = false),
                TestCase(input = "Testing", expectedOutput = "estin", isHidden = true)
            )
        )
        problemDao.insertProblem(trimFirstLast)

        // Problem 93: Extract Substring Between Indices
        val extractSubstring = ProblemEntity(
            id = 93,
            title = "Extract Substring Between Indices",
            description = """
                Given a string s and two indices start and end (inclusive), return the substring from start to end.
                
                If indices are invalid, return an empty string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • -10⁴ <= start, end <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"abcdef\", start = 1, end = 3",
                    output = "\"bcd\"",
                    explanation = "Characters at indices 1, 2, 3"
                ),
                Example(
                    input = "s = \"hello\", start = 0, end = 4",
                    output = "\"hello\"",
                    explanation = "Whole string"
                )
            ),
            hints = listOf(
                "Validate indices before extracting.",
                "Make sure start and end are within bounds."
            ),
            editorial = """
                ## Approach: Substring Extraction
                
                Validate indices, then use substring method.
                
                **Algorithm:**
                1. If s is empty or start < 0 or end >= s.length or start > end, return ""
                2. Return s.substring(start, end + 1)
                
                **Complexity Analysis:**
                • Time complexity: O(k) where k is substring length
                • Space complexity: O(k)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 92.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun extractSubstring(s: String, start: Int, end: Int): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String extractSubstring(String s, int start, int end) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def extractSubstring(s: str, start: int, end: int) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {number} start
 * @param {number} end
 * @return {string}
 */
var extractSubstring = function(s, start, end) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string extractSubstring(string s, int start, int end) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "abcdef\n1\n3", expectedOutput = "bcd", isHidden = false),
                TestCase(input = "hello\n0\n4", expectedOutput = "hello", isHidden = false),
                TestCase(input = "test\n2\n2", expectedOutput = "s", isHidden = false),
                TestCase(input = "abc\n-1\n1", expectedOutput = "", isHidden = true)
            )
        )
        problemDao.insertProblem(extractSubstring)

        // Problem 94: Count Occurrences of Character
        val countChar = ProblemEntity(
            id = 94,
            title = "Count Occurrences of a Character",
            description = """
                Given a string s and a character c, count how many times c appears in s.
                
                The comparison should be case-sensitive.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • c is a single ASCII character
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\", c = 'l'",
                    output = "2",
                    explanation = "'l' appears twice"
                ),
                Example(
                    input = "s = \"Mississippi\", c = 's'",
                    output = "4",
                    explanation = "'s' appears 4 times"
                )
            ),
            hints = listOf(
                "Iterate through string and compare each character.",
                "Use a counter variable to keep track."
            ),
            editorial = """
                ## Approach: Loop and Count
                
                Iterate through string and count matches.
                
                **Algorithm:**
                1. Initialize count = 0
                2. For each char in s:
                   - If char == c, count++
                3. Return count
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 96.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun countCharOccurrences(s: String, c: Char): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countCharOccurrences(String s, char c) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def countCharOccurrences(s: str, c: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {character} c
 * @return {number}
 */
var countCharOccurrences = function(s, c) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countCharOccurrences(string s, char c) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello\nl", expectedOutput = "2", isHidden = false),
                TestCase(input = "Mississippi\ns", expectedOutput = "4", isHidden = false),
                TestCase(input = "abc\nd", expectedOutput = "0", isHidden = false),
                TestCase(input = "AAAA\na", expectedOutput = "0", isHidden = true)
            )
        )
        problemDao.insertProblem(countChar)

        // Problem 95: Check if String is Empty or Blank
        val isBlank = ProblemEntity(
            id = 95,
            title = "Check if String is Empty or Blank",
            description = """
                Given a string s, return true if the string is empty or contains only whitespace characters.
                
                Whitespace characters include spaces, tabs, newlines, etc.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"\"",
                    output = "true",
                    explanation = "Empty string"
                ),
                Example(
                    input = "s = \"   \"",
                    output = "true",
                    explanation = "Only spaces"
                ),
                Example(
                    input = "s = \"hello\"",
                    output = "false",
                    explanation = "Contains non-whitespace characters"
                )
            ),
            hints = listOf(
                "Check if string length is 0 after trimming whitespace.",
                "Alternatively, iterate and check if any non-whitespace exists."
            ),
            editorial = """
                ## Approach: Trim and Check
                
                Trim whitespace and check if result is empty.
                
                **Algorithm:**
                1. Return s.trim().isEmpty()
                
                **Alternative:** Iterate and return false if any non-whitespace found.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) or O(n) for trim
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 97.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun isBlank(s: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isBlank(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isBlank(s: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {boolean}
 */
var isBlank = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isBlank(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "", expectedOutput = "true", isHidden = false),
                TestCase(input = "   ", expectedOutput = "true", isHidden = false),
                TestCase(input = "hello", expectedOutput = "false", isHidden = false),
                TestCase(input = "  hello  ", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(isBlank)

        // Problem 96: Convert String to Uppercase
        val toUpperCase = ProblemEntity(
            id = 96,
            title = "Convert String to Uppercase",
            description = """
                Given a string s, convert all lowercase letters to uppercase.
                
                Non-letter characters should remain unchanged.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"Hello World\"",
                    output = "\"HELLO WORLD\"",
                    explanation = "All letters converted to uppercase"
                ),
                Example(
                    input = "s = \"123abc!\"",
                    output = "\"123ABC!\"",
                    explanation = "Numbers and punctuation unchanged"
                )
            ),
            hints = listOf(
                "Use built-in toUpperCase() function.",
                "Or manually convert using ASCII values: 'a' to 'A' subtracts 32."
            ),
            editorial = """
                ## Approach: Built-in Method
                
                Use language's built-in uppercase conversion.
                
                **Algorithm:**
                1. Return s.toUpperCase()
                
                **Manual Approach:**
                - Iterate through characters
                - If char is lowercase, convert to uppercase (char - 32)
                - Build result string
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 99.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun toUpperCase(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String toUpperCase(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def toUpperCase(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var toUpperCase = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string toUpperCase(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "Hello World", expectedOutput = "HELLO WORLD", isHidden = false),
                TestCase(input = "123abc!", expectedOutput = "123ABC!", isHidden = false),
                TestCase(input = "lowercase", expectedOutput = "LOWERCASE", isHidden = false),
                TestCase(input = "UPPER", expectedOutput = "UPPER", isHidden = true)
            )
        )
        problemDao.insertProblem(toUpperCase)

                // ==================== ADDITIONAL STRING OPERATIONS (97-110) ====================

        // Problem 97: Convert String to Lowercase
        val toLowerCase = ProblemEntity(
            id = 97,
            title = "Convert String to Lowercase",
            description = """
                Given a string s, convert all uppercase letters to lowercase.
                
                Non-letter characters should remain unchanged.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s consists of ASCII characters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"Hello World\"",
                    output = "\"hello world\"",
                    explanation = "All letters converted to lowercase"
                ),
                Example(
                    input = "s = \"123ABC!\"",
                    output = "\"123abc!\"",
                    explanation = "Numbers and punctuation unchanged"
                )
            ),
            hints = listOf(
                "Use built-in toLowerCase() function.",
                "Or manually convert using ASCII values: 'A' to 'a' adds 32."
            ),
            editorial = """
                ## Approach: Built-in Method
                
                Use language's built-in lowercase conversion.
                
                **Algorithm:**
                1. Return s.toLowerCase()
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 99.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun toLowerCase(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String toLowerCase(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def toLowerCase(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var toLowerCase = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string toLowerCase(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "Hello World", expectedOutput = "hello world", isHidden = false),
                TestCase(input = "123ABC!", expectedOutput = "123abc!", isHidden = false),
                TestCase(input = "UPPERCASE", expectedOutput = "uppercase", isHidden = false),
                TestCase(input = "lower", expectedOutput = "lower", isHidden = true)
            )
        )
        problemDao.insertProblem(toLowerCase)

        // Problem 98: Get String Length
        val stringLength = ProblemEntity(
            id = 98,
            title = "Get String Length",
            description = """
                Given a string s, return its length (number of characters).
                
                Empty string should return 0.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "5",
                    explanation = "String has 5 characters"
                ),
                Example(
                    input = "s = \"\"",
                    output = "0",
                    explanation = "Empty string has length 0"
                )
            ),
            hints = listOf(
                "Use built-in length property/method.",
                "No iteration needed - it's O(1) operation in most languages."
            ),
            editorial = """
                ## Approach: Direct Access
                
                Return the length property of the string.
                
                **Algorithm:**
                1. Return s.length
                
                **Complexity Analysis:**
                • Time complexity: O(1)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 5,
            acceptanceRate = 100.0f,
            xpReward = 5,
            starterCode = mapOf(
                "Kotlin" to """
fun getStringLength(s: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int getStringLength(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def getStringLength(s: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var getStringLength = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int getStringLength(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "5", isHidden = false),
                TestCase(input = "", expectedOutput = "0", isHidden = false),
                TestCase(input = "a", expectedOutput = "1", isHidden = false),
                TestCase(input = "Hello World!", expectedOutput = "12", isHidden = true)
            )
        )
        problemDao.insertProblem(stringLength)

        // Problem 99: Get Character at Index
        val charAtIndex = ProblemEntity(
            id = 99,
            title = "Get Character at Index",
            description = """
                Given a string s and an index i, return the character at index i.
                
                If index is out of range, return null or throw an exception.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • -10⁴ <= i <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\", i = 1",
                    output = "'e'",
                    explanation = "Character at index 1 is 'e'"
                ),
                Example(
                    input = "s = \"world\", i = 4",
                    output = "'d'",
                    explanation = "Character at index 4 is 'd'"
                )
            ),
            hints = listOf(
                "Check if index is within bounds before accessing.",
                "Use charAt() method in most languages."
            ),
            editorial = """
                ## Approach: Direct Access with Validation
                
                Check bounds, then return character at index.
                
                **Algorithm:**
                1. If i < 0 or i >= s.length, return null
                2. Return s[i]
                
                **Complexity Analysis:**
                • Time complexity: O(1)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 93.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun charAtIndex(s: String, i: Int): Char? {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public Character charAtIndex(String s, int i) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def charAtIndex(s: str, i: int) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {number} i
 * @return {string|null}
 */
var charAtIndex = function(s, i) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    char charAtIndex(string s, int i) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello\n1", expectedOutput = "e", isHidden = false),
                TestCase(input = "world\n4", expectedOutput = "d", isHidden = false),
                TestCase(input = "abc\n0", expectedOutput = "a", isHidden = false),
                TestCase(input = "test\n10", expectedOutput = "null", isHidden = true)
            )
        )
        problemDao.insertProblem(charAtIndex)

        // Problem 100: Check if String Starts With Prefix
        val startsWith = ProblemEntity(
            id = 100,
            title = "Check if String Starts With Prefix",
            description = """
                Given a string s and a prefix string p, return true if s starts with p.
                
                If p is empty, return true (empty string is prefix of everything).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length, p.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\", p = \"hello\"",
                    output = "true",
                    explanation = "String starts with 'hello'"
                ),
                Example(
                    input = "s = \"world\", p = \"hello\"",
                    output = "false",
                    explanation = "String does not start with 'hello'"
                )
            ),
            hints = listOf(
                "Check if p is longer than s, then return false.",
                "Compare first p.length characters."
            ),
            editorial = """
                ## Approach: Prefix Comparison
                
                Compare first n characters where n is prefix length.
                
                **Algorithm:**
                1. If p.length > s.length, return false
                2. If p is empty, return true
                3. For i from 0 to p.length-1:
                   - If s[i] != p[i], return false
                4. Return true
                
                **Alternative:** Use built-in startsWith method.
                
                **Complexity Analysis:**
                • Time complexity: O(min(m, n))
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 94.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun startsWith(s: String, prefix: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean startsWith(String s, String prefix) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def startsWith(s: str, prefix: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {string} prefix
 * @return {boolean}
 */
var startsWith = function(s, prefix) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool startsWith(string s, string prefix) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world\nhello", expectedOutput = "true", isHidden = false),
                TestCase(input = "world\nhello", expectedOutput = "false", isHidden = false),
                TestCase(input = "abc\n", expectedOutput = "true", isHidden = false),
                TestCase(input = "test\ntesting", expectedOutput = "false", isHidden = true)
            )
        )
        problemDao.insertProblem(startsWith)

        // Problem 101: Check if String Ends With Suffix
        val endsWith = ProblemEntity(
            id = 101,
            title = "Check if String Ends With Suffix",
            description = """
                Given a string s and a suffix string suf, return true if s ends with suf.
                
                If suf is empty, return true (empty string is suffix of everything).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length, suf.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\", suf = \"world\"",
                    output = "true",
                    explanation = "String ends with 'world'"
                ),
                Example(
                    input = "s = \"hello\", suf = \"world\"",
                    output = "false",
                    explanation = "String does not end with 'world'"
                )
            ),
            hints = listOf(
                "Check if suf is longer than s, then return false.",
                "Compare last suf.length characters."
            ),
            editorial = """
                ## Approach: Suffix Comparison
                
                Compare last n characters where n is suffix length.
                
                **Algorithm:**
                1. If suf.length > s.length, return false
                2. If suf is empty, return true
                3. For i from 0 to suf.length-1:
                   - If s[s.length - suf.length + i] != suf[i], return false
                4. Return true
                
                **Alternative:** Use built-in endsWith method.
                
                **Complexity Analysis:**
                • Time complexity: O(min(m, n))
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 94.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun endsWith(s: String, suffix: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean endsWith(String s, String suffix) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def endsWith(s: str, suffix: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {string} suffix
 * @return {boolean}
 */
var endsWith = function(s, suffix) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool endsWith(string s, string suffix) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world\nworld", expectedOutput = "true", isHidden = false),
                TestCase(input = "hello\nworld", expectedOutput = "false", isHidden = false),
                TestCase(input = "abc\n", expectedOutput = "true", isHidden = false),
                TestCase(input = "testing\ning", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(endsWith)

        // Problem 102: Repeat String N Times
        val repeatString = ProblemEntity(
            id = 102,
            title = "Repeat String N Times",
            description = """
                Given a string s and an integer n, return a new string that repeats s n times.
                
                If n = 0, return an empty string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10³
                • 0 <= n <= 10³
                • Result length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"abc\", n = 3",
                    output = "\"abcabcabc\"",
                    explanation = "Repeat 'abc' three times"
                ),
                Example(
                    input = "s = \"hi\", n = 2",
                    output = "\"hihi\"",
                    explanation = "Repeat 'hi' twice"
                )
            ),
            hints = listOf(
                "Use StringBuilder with a loop.",
                "Or use built-in repeat function if available."
            ),
            editorial = """
                ## Approach: Loop and Append
                
                Build result by appending s n times.
                
                **Algorithm:**
                1. Initialize StringBuilder result
                2. For i from 0 to n-1:
                   - Append s to result
                3. Return result.toString()
                
                **Complexity Analysis:**
                • Time complexity: O(n * m) where m is length of s
                • Space complexity: O(n * m)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 92.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun repeatString(s: String, n: Int): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String repeatString(String s, int n) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def repeatString(s: str, n: int) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {number} n
 * @return {string}
 */
var repeatString = function(s, n) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string repeatString(string s, int n) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "abc\n3", expectedOutput = "abcabcabc", isHidden = false),
                TestCase(input = "hi\n2", expectedOutput = "hihi", isHidden = false),
                TestCase(input = "a\n0", expectedOutput = "", isHidden = false),
                TestCase(input = "hello\n1", expectedOutput = "hello", isHidden = true)
            )
        )
        problemDao.insertProblem(repeatString)

        // Problem 103: Check if String Contains Substring
        val containsSubstring = ProblemEntity(
            id = 103,
            title = "Check if String Contains Substring",
            description = """
                Given a string s and a substring sub, return true if sub appears anywhere in s.
                
                If sub is empty, return true (empty string is substring of everything).
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length, sub.length <= 10³
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\", sub = \"world\"",
                    output = "true",
                    explanation = "'world' is in the string"
                ),
                Example(
                    input = "s = \"hello\", sub = \"abc\"",
                    output = "false",
                    explanation = "'abc' not in string"
                )
            ),
            hints = listOf(
                "Use built-in contains or indexOf method.",
                "Or implement manually by checking all possible starting positions."
            ),
            editorial = """
                ## Approach: Built-in or Manual Search
                
                Use built-in contains method or manual search.
                
                **Algorithm:**
                1. If sub is empty, return true
                2. If sub.length > s.length, return false
                3. For i from 0 to s.length - sub.length:
                   - If s.substring(i, i + sub.length) == sub, return true
                4. Return false
                
                **Complexity Analysis:**
                • Time complexity: O(m * n) worst case
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 91.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun containsSubstring(s: String, sub: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean containsSubstring(String s, String sub) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def containsSubstring(s: str, sub: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {string} sub
 * @return {boolean}
 */
var containsSubstring = function(s, sub) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool containsSubstring(string s, string sub) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world\nworld", expectedOutput = "true", isHidden = false),
                TestCase(input = "hello\nabc", expectedOutput = "false", isHidden = false),
                TestCase(input = "abc\n", expectedOutput = "true", isHidden = false),
                TestCase(input = "banana\nana", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(containsSubstring)

        // Problem 104: Find First Index of Substring
        val indexOfSubstring = ProblemEntity(
            id = 104,
            title = "Find First Index of Substring",
            description = """
                Given a string s and a substring sub, return the first index where sub appears in s.
                
                If sub is not found, return -1. If sub is empty, return 0.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length, sub.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\", sub = \"world\"",
                    output = "6",
                    explanation = "'world' starts at index 6"
                ),
                Example(
                    input = "s = \"hello\", sub = \"abc\"",
                    output = "-1",
                    explanation = "Substring not found"
                )
            ),
            hints = listOf(
                "Use built-in indexOf method.",
                "Or implement manually by checking all possible starting positions."
            ),
            editorial = """
                ## Approach: Manual Search
                
                Check each possible starting position.
                
                **Algorithm:**
                1. If sub is empty, return 0
                2. If sub.length > s.length, return -1
                3. For i from 0 to s.length - sub.length:
                   - If s.substring(i, i + sub.length) == sub, return i
                4. Return -1
                
                **Complexity Analysis:**
                • Time complexity: O(m * n) worst case
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 89.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun indexOfSubstring(s: String, sub: String): Int {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int indexOfSubstring(String s, String sub) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def indexOfSubstring(s: str, sub: str) -> int:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {string} sub
 * @return {number}
 */
var indexOfSubstring = function(s, sub) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int indexOfSubstring(string s, string sub) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world\nworld", expectedOutput = "6", isHidden = false),
                TestCase(input = "hello\nabc", expectedOutput = "-1", isHidden = false),
                TestCase(input = "abc\n", expectedOutput = "0", isHidden = false),
                TestCase(input = "banana\nana", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(indexOfSubstring)

        // Problem 105: Remove All Occurrences of Character
        val removeAllChar = ProblemEntity(
            id = 105,
            title = "Remove All Occurrences of a Character",
            description = """
                Given a string s and a character c, remove all occurrences of c from s.
                
                Return the resulting string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • c is a single ASCII character
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\", c = 'l'",
                    output = "\"heo\"",
                    explanation = "Remove both 'l' characters"
                ),
                Example(
                    input = "s = \"banana\", c = 'a'",
                    output = "\"bnn\"",
                    explanation = "Remove all 'a' characters"
                )
            ),
            hints = listOf(
                "Use StringBuilder and append characters that are not c.",
                "Or use replace method: s.replace(String.valueOf(c), \"\")"
            ),
            editorial = """
                ## Approach: Filter Characters
                
                Build result string excluding target character.
                
                **Algorithm:**
                1. Initialize StringBuilder result
                2. For each char in s:
                   - If char != c, append to result
                3. Return result.toString()
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 94.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun removeAllOccurrences(s: String, c: Char): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String removeAllOccurrences(String s, char c) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def removeAllOccurrences(s: str, c: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @param {character} c
 * @return {string}
 */
var removeAllOccurrences = function(s, c) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string removeAllOccurrences(string s, char c) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello\nl", expectedOutput = "heo", isHidden = false),
                TestCase(input = "banana\na", expectedOutput = "bnn", isHidden = false),
                TestCase(input = "abc\nd", expectedOutput = "abc", isHidden = false),
                TestCase(input = "aaaa\na", expectedOutput = "", isHidden = true)
            )
        )
        problemDao.insertProblem(removeAllChar)

        // Problem 106: Swap First and Last Character
        val swapFirstLast = ProblemEntity(
            id = 106,
            title = "Swap First and Last Character",
            description = """
                Given a string s, swap the first and last character.
                
                If string length is less than 2, return the original string.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "\"oellh\"",
                    explanation = "Swap 'h' and 'o'"
                ),
                Example(
                    input = "s = \"ab\"",
                    output = "\"ba\"",
                    explanation = "Swap 'a' and 'b'"
                )
            ),
            hints = listOf(
                "Convert string to char array, swap first and last, then convert back.",
                "Or use substring with concatenation."
            ),
            editorial = """
                ## Approach: Character Array
                
                Convert to char array, swap first and last, convert back.
                
                **Algorithm:**
                1. If s.length < 2, return s
                2. Convert s to char array
                3. Swap arr[0] and arr[s.length-1]
                4. Return new string from array
                
                **Alternative:** Use substring: s[s.length-1] + s.substring(1, s.length-1) + s[0]
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 90.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun swapFirstLast(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String swapFirstLast(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def swapFirstLast(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var swapFirstLast = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string swapFirstLast(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "oellh", isHidden = false),
                TestCase(input = "ab", expectedOutput = "ba", isHidden = false),
                TestCase(input = "a", expectedOutput = "a", isHidden = false),
                TestCase(input = "world", expectedOutput = "dorlw", isHidden = true)
            )
        )
        problemDao.insertProblem(swapFirstLast)

        // Problem 107: Reverse Words in String
        val reverseWords = ProblemEntity(
            id = 107,
            title = "Reverse Words in String",
            description = """
                Given a string s containing words separated by spaces, reverse the order of the words.
                
                For example, "hello world" becomes "world hello".
            """.trimIndent(),
            difficulty = "Medium",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 10⁴
                • s contains only letters and spaces
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "\"world hello\"",
                    explanation = "Words reversed"
                ),
                Example(
                    input = "s = \"the sky is blue\"",
                    output = "\"blue is sky the\"",
                    explanation = "All words reversed"
                )
            ),
            hints = listOf(
                "Split the string by spaces into an array of words.",
                "Reverse the array, then join with spaces."
            ),
            editorial = """
                ## Approach: Split and Reverse
                
                Split into words, reverse order, join back.
                
                **Algorithm:**
                1. Split s by spaces into words array
                2. Reverse the words array
                3. Join words with single space
                4. Return result
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n)
            """.trimIndent(),
            timeEstimateMinutes = 20,
            acceptanceRate = 83.0f,
            xpReward = 20,
            starterCode = mapOf(
                "Kotlin" to """
fun reverseWords(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String reverseWords(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def reverseWords(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var reverseWords = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string reverseWords(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "world hello", isHidden = false),
                TestCase(input = "the sky is blue", expectedOutput = "blue is sky the", isHidden = false),
                TestCase(input = "a", expectedOutput = "a", isHidden = false),
                TestCase(input = "hello world from kotlin", expectedOutput = "kotlin from world hello", isHidden = true)
            )
        )
        problemDao.insertProblem(reverseWords)

        // Problem 108: Check if String is Palindrome (Ignoring Case)
        val isPalindromeIgnoreCase = ProblemEntity(
            id = 108,
            title = "Check Palindrome Ignoring Case",
            description = """
                Given a string s, determine if it is a palindrome, ignoring case sensitivity.
                
                For example, "Racecar" and "racecar" are considered the same.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 10⁵
                • s consists of English letters
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"Racecar\"",
                    output = "true",
                    explanation = "Case-insensitive: RACECAR reads same backward"
                ),
                Example(
                    input = "s = \"Hello\"",
                    output = "false",
                    explanation = "Hello reversed is olleH, not same"
                )
            ),
            hints = listOf(
                "Convert entire string to lowercase or uppercase first.",
                "Then use two pointers to compare from both ends."
            ),
            editorial = """
                ## Approach: Case-Insensitive Two Pointers
                
                Convert to lower/upper case, then compare from both ends.
                
                **Algorithm:**
                1. Convert s to lowercase
                2. Initialize left = 0, right = s.length - 1
                3. While left < right:
                   - If s[left] != s[right], return false
                   - left++, right--
                4. Return true
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(n) for conversion or O(1) if compare during conversion
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 87.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun isPalindromeIgnoreCase(s: String): Boolean {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public boolean isPalindromeIgnoreCase(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def isPalindromeIgnoreCase(s: str) -> bool:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {boolean}
 */
var isPalindromeIgnoreCase = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    bool isPalindromeIgnoreCase(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "Racecar", expectedOutput = "true", isHidden = false),
                TestCase(input = "Hello", expectedOutput = "false", isHidden = false),
                TestCase(input = "Aa", expectedOutput = "true", isHidden = false),
                TestCase(input = "MadAm", expectedOutput = "true", isHidden = true)
            )
        )
        problemDao.insertProblem(isPalindromeIgnoreCase)

        // Problem 109: Count Words in String (Without Split)
        val countWordsManual = ProblemEntity(
            id = 109,
            title = "Count Words in String (Without Split)",
            description = """
                Given a string s containing words separated by spaces, count the number of words.
                
                Do not use the split function - implement it manually.
                
                Words are sequences of non-space characters.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 0 <= s.length <= 10⁴
                • s contains letters and spaces only
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello world\"",
                    output = "2",
                    explanation = "Two words"
                ),
                Example(
                    input = "s = \"  hello   world  \"",
                    output = "2",
                    explanation = "Extra spaces ignored"
                )
            ),
            hints = listOf(
                "Track when we are inside a word vs outside.",
                "Count transitions from space to non-space."
            ),
            editorial = """
                ## Approach: State Tracking
                
                Track whether we are currently inside a word.
                
                **Algorithm:**
                1. Initialize count = 0, inWord = false
                2. For each char in s:
                   - If char != ' ' and not inWord:
                     - count++
                     - inWord = true
                   - Else if char == ' ':
                     - inWord = false
                3. Return count
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 15,
            acceptanceRate = 86.0f,
            xpReward = 15,
            starterCode = mapOf(
                "Kotlin" to """
fun countWordsManual(s: String): Int {
    // Write your code here (without using split)
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public int countWordsManual(String s) {
        // Write your code here (without using split)
        
    }
}
                """.trimIndent(),
                "Python" to """
def countWordsManual(s: str) -> int:
    # Write your code here (without using split)
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {number}
 */
var countWordsManual = function(s) {
    // Write your code here (without using split)
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    int countWordsManual(string s) {
        // Write your code here (without using split)
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello world", expectedOutput = "2", isHidden = false),
                TestCase(input = "  hello   world  ", expectedOutput = "2", isHidden = false),
                TestCase(input = "", expectedOutput = "0", isHidden = false),
                TestCase(input = "one", expectedOutput = "1", isHidden = true)
            )
        )
        problemDao.insertProblem(countWordsManual)

        // Problem 110: Get Middle Character
        val middleCharacter = ProblemEntity(
            id = 110,
            title = "Get Middle Character",
            description = """
                Given a string s, return the middle character(s).
                
                If the string length is odd, return the single middle character.
                If the string length is even, return the two middle characters.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "String",
            constraints = """
                • 1 <= s.length <= 1000
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "s = \"hello\"",
                    output = "\"l\"",
                    explanation = "Length 5, middle character is 'l'"
                ),
                Example(
                    input = "s = \"test\"",
                    output = "\"es\"",
                    explanation = "Length 4, middle two characters are 'e' and 's'"
                )
            ),
            hints = listOf(
                "Find the middle index: mid = s.length / 2",
                "If length is odd, return s[mid]",
                "If length is even, return s[mid-1] + s[mid]"
            ),
            editorial = """
                ## Approach: Index Calculation
                
                Calculate middle index based on length parity.
                
                **Algorithm:**
                1. n = s.length
                2. mid = n / 2
                3. If n % 2 == 1:
                   - Return s[mid]
                4. Else:
                   - Return s[mid-1] + s[mid]
                
                **Complexity Analysis:**
                • Time complexity: O(1)
                • Space complexity: O(1)
            """.trimIndent(),
            timeEstimateMinutes = 10,
            acceptanceRate = 93.0f,
            xpReward = 10,
            starterCode = mapOf(
                "Kotlin" to """
fun getMiddleCharacter(s: String): String {
    // Write your code here
    
}
                """.trimIndent(),
                "Java" to """
class Solution {
    public String getMiddleCharacter(String s) {
        // Write your code here
        
    }
}
                """.trimIndent(),
                "Python" to """
def getMiddleCharacter(s: str) -> str:
    # Write your code here
    pass
                """.trimIndent(),
                "JavaScript" to """
/**
 * @param {string} s
 * @return {string}
 */
var getMiddleCharacter = function(s) {
    // Write your code here
    
};
                """.trimIndent(),
                "C++" to """
class Solution {
public:
    string getMiddleCharacter(string s) {
        // Write your code here
        
    }
};
                """.trimIndent()
            ),
            testCases = listOf(
                TestCase(input = "hello", expectedOutput = "l", isHidden = false),
                TestCase(input = "test", expectedOutput = "es", isHidden = false),
                TestCase(input = "a", expectedOutput = "a", isHidden = false),
                TestCase(input = "middle", expectedOutput = "dd", isHidden = true)
            )
        )
        problemDao.insertProblem(middleCharacter)
        
    }
        private suspend fun seedBadges() {
            val badges = listOf(
                BadgeEntity(
                    id = "FIRST_SOLVE",
                    name = "First Steps",
                    description = "Solve your first problem",
                    iconType = "STAR",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "STREAK_3",
                    name = "On Fire",
                    description = "Maintain a 3-day streak",
                    iconType = "FLAME",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "STREAK_7",
                    name = "Week Warrior",
                    description = "Maintain a 7-day streak",
                    iconType = "FLAME",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "STREAK_30",
                    name = "Monthly Master",
                    description = "Maintain a 30-day streak",
                    iconType = "FLAME",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "STREAK_100",
                    name = "Century Club",
                    description = "Maintain a 100-day streak",
                    iconType = "TROPHY",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "STREAK_365",
                    name = "Year Legend",
                    description = "Maintain a 365-day streak",
                    iconType = "TROPHY",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "SOLVED_10",
                    name = "Problem Solver",
                    description = "Solve 10 problems",
                    iconType = "STAR",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "SOLVED_50",
                    name = "Code Master",
                    description = "Solve 50 problems",
                    iconType = "TROPHY",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "SOLVED_100",
                    name = "Algorithm Expert",
                    description = "Solve 100 problems",
                    iconType = "TROPHY",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "SPEED_DEMON",
                    name = "Speed Demon",
                    description = "Solve a Hard problem in under 10 minutes",
                    iconType = "LIGHTNING",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "POLYGLOT",
                    name = "Polyglot",
                    description = "Solve problems in all 5 languages",
                    iconType = "GLOBE",
                    isUnlocked = false
                ),
                BadgeEntity(
                    id = "NIGHT_OWL",
                    name = "Night Owl",
                    description = "Solve a problem between midnight and 4 AM",
                    iconType = "OWL",
                    isUnlocked = false
                )
            )

            badgeDao.insertBadges(badges)
        }
    }