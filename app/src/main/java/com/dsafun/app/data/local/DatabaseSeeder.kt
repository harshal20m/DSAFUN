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
        // Arrays
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
                "A really brute force way would be to search for all possible pairs of numbers but that would be too slow. Again, it's best to try out brute force solutions for just for completeness. It is from these brute force solutions that you can come up with optimizations.",
                "So, if we fix one of the numbers, say x, we have to scan the entire array to find the next number y which is value - x where value is the input parameter. Can we change our array somehow so that this search becomes faster?"
            ),
            editorial = """
                ## Approach 1: Brute Force
                
                The brute force approach is simple. Loop through each element x and find if there is another value that equals to target - x.
                
                **Complexity Analysis:**
                • Time complexity: O(n²)
                • Space complexity: O(1)
                
                ## Approach 2: Hash Map (Optimal)
                
                To improve our runtime complexity, we need a more efficient way to check if the complement exists in the array. If the complement exists, we need to get its index. What is the best way to maintain a mapping of each element in the array to its index? A hash map.
                
                We can reduce the lookup time from O(n) to O(1) by trading space for speed. A hash map is well suited for this purpose because it supports fast lookup in near constant time.
                
                **Algorithm:**
                1. Create a hash map to store value -> index mapping
                2. Iterate through the array
                3. For each element, check if (target - current element) exists in the map
                4. If it exists, return the indices
                5. Otherwise, add the current element and its index to the map
                
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
                TestCase(
                    input = "[2,7,11,15]\n9",
                    expectedOutput = "[0,1]",
                    isHidden = false
                ),
                TestCase(
                    input = "[3,2,4]\n6",
                    expectedOutput = "[1,2]",
                    isHidden = false
                ),
                TestCase(
                    input = "[3,3]\n6",
                    expectedOutput = "[0,1]",
                    isHidden = true
                )
            )
        )

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
                "The aim is to maximize the area formed between the vertical lines. The area of any container is calculated using the shorter line as length and the distance between the lines as the width of the rectangle.",
                "Start with the maximum width container and go to a shorter width container if there is a vertical line longer than the current containers shorter line. This way we are compromising on the width but we are looking forward to a longer length container."
            ),
            editorial = """
                ## Approach: Two Pointer
                
                The intuition behind this approach is that the area formed between the lines will always be limited by the height of the shorter line. Further, the farther the lines, the more will be the area obtained.
                
                We take two pointers, one at the beginning and one at the end of the array constituting the length of the lines. Further, we maintain a variable maxarea to store the maximum area obtained till now. At every step, we find out the area formed between them, update maxarea and move the pointer pointing to the shorter line towards the other end by one step.
                
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
                TestCase(
                    input = "[1,8,6,2,5,4,8,3,7]",
                    expectedOutput = "49",
                    isHidden = false
                ),
                TestCase(
                    input = "[1,1]",
                    expectedOutput = "1",
                    isHidden = false
                )
            )
        )

        // Strings
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
                "Can you think of a way to check if a string is a palindrome using two pointers?",
                "Remember to handle the case where the string contains non-alphanumeric characters."
            ),
            editorial = """
                ## Approach: Two Pointer
                
                We can use two pointers to check if the string is a palindrome. One pointer starts at the beginning and the other at the end. We compare characters at both pointers, skipping non-alphanumeric characters and converting to lowercase.
                
                **Algorithm:**
                1. Initialize left pointer at start, right pointer at end
                2. Skip non-alphanumeric characters from both ends
                3. Compare characters (case-insensitive)
                4. If they don't match, return false
                5. Move pointers inward and repeat
                6. If all characters match, return true
                
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
                TestCase(
                    input = "\"race a car\"",
                    expectedOutput = "false",
                    isHidden = false
                )
            )
        )

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
                "When you encounter a duplicate character, shrink the window from the left until the duplicate is removed."
            ),
            editorial = """
                ## Approach: Sliding Window with Hash Set
                
                We use a sliding window approach with a hash set to keep track of characters in the current window. When we encounter a duplicate, we shrink the window from the left.
                
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
                TestCase(
                    input = "\"abcabcbb\"",
                    expectedOutput = "3",
                    isHidden = false
                ),
                TestCase(
                    input = "\"bbbbb\"",
                    expectedOutput = "1",
                    isHidden = false
                ),
                TestCase(
                    input = "\"pwwkew\"",
                    expectedOutput = "3",
                    isHidden = false
                )
            )
        )

        // Trees
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
                "Think about using recursion. What is the base case?",
                "The depth of a tree is 1 + max(depth of left subtree, depth of right subtree)."
            ),
            editorial = """
                ## Approach 1: Recursive DFS
                
                The intuition is that the depth of a tree is 1 plus the maximum depth of its left and right subtrees.
                
                **Algorithm:**
                1. Base case: if root is null, return 0
                2. Recursively find depth of left subtree
                3. Recursively find depth of right subtree
                4. Return 1 + max(left depth, right depth)
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(h) where h is height (recursion stack)
                
                ## Approach 2: Iterative BFS
                
                Use level-order traversal with a queue to count levels.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(w) where w is max width
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
                TestCase(
                    input = "[3,9,20,null,null,15,7]",
                    expectedOutput = "3",
                    isHidden = false
                ),
                TestCase(
                    input = "[1,null,2]",
                    expectedOutput = "2",
                    isHidden = false
                )
            )
        )

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
                "Process nodes level by level. Track the number of nodes at each level."
            ),
            editorial = """
                ## Approach: BFS with Queue
                
                We use a queue to perform level-order traversal. For each level, we process all nodes at that level before moving to the next.
                
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
                TestCase(
                    input = "[1]",
                    expectedOutput = "[[1]]",
                    isHidden = false
                )
            )
        )

        // Graphs
        val numIslands = ProblemEntity(
            id = 7,
            title = "Number of Islands",
            description = """
                Given an m x n 2D binary grid `grid` which represents a map of '1's (land) and '0's (water), return the number of islands.
                
                An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.
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
                
                We iterate through the grid. When we find a '1', we increment the island count and use DFS to mark all connected land cells as visited.
                
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

        val validPath = ProblemEntity(
            id = 8,
            title = "Find if Path Exists in Graph",
            description = """
                There is a bi-directional graph with `n` vertices, where each vertex is labeled from `0` to `n - 1` (inclusive). The edges in the graph are represented as a 2D integer array `edges`, where each `edges[i] = [ui, vi]` denotes a bi-directional edge between vertex `ui` and vertex `vi`.
                
                Given `edges` and the integers `n`, `source`, and `destination`, return `true` if there is a valid path from `source` to `destination`, or `false` otherwise.
            """.trimIndent(),
            difficulty = "Easy",
            topic = "Graph",
            constraints = """
                • 1 <= n <= 2 * 10⁵
                • 0 <= edges.length <= 2 * 10⁵
                • edges[i].length == 2
                • 0 <= ui, vi <= n - 1
                • ui != vi
                • 0 <= source, destination <= n - 1
                • There are no duplicate edges.
                • There are no self edges.
            """.trimIndent(),
            examples = listOf(
                Example(
                    input = "n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2",
                    output = "true",
                    explanation = "There are two paths from vertex 0 to vertex 2: 0 → 1 → 2 and 0 → 2"
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
                
                We build an adjacency list and use BFS or DFS to check if there's a path from source to destination.
                
                **Algorithm:**
                1. Build adjacency list from edges
                2. Use BFS/DFS starting from source
                3. Mark visited nodes
                4. If we reach destination, return true
                5. If search completes without finding destination, return false
                
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

        // DP
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
                    explanation = "There are two ways to climb to the top: 1. 1 step + 1 step, 2. 2 steps"
                ),
                Example(
                    input = "n = 3",
                    output = "3",
                    explanation = "There are three ways to climb to the top: 1. 1 step + 1 step + 1 step, 2. 1 step + 2 steps, 3. 2 steps + 1 step"
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
                2. For each step i from 3 to n:
                   - dp[i] = dp[i-1] + dp[i-2]
                3. Return dp[n]
                
                **Optimization:** We only need the last two values, so we can use O(1) space.
                
                **Complexity Analysis:**
                • Time complexity: O(n)
                • Space complexity: O(1) with optimization
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
                TestCase(
                    input = "2",
                    expectedOutput = "2",
                    isHidden = false
                ),
                TestCase(
                    input = "3",
                    expectedOutput = "3",
                    isHidden = false
                )
            )
        )

        val coinChange = ProblemEntity(
            id = 10,
            title = "Coin Change",
            description = """
                You are given an integer array `coins` representing coins of different denominations and an integer `amount` representing a total amount of money.
                
                Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return `-1`.
                
                You may assume that you have an infinite number of each kind of coin.
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
                    explanation = "Amount 3 cannot be made up with coins of denomination 2."
                )
            ),
            hints = listOf(
                "Think about the problem recursively. What is the minimum number of coins needed for amount - coin[i]?",
                "Use dynamic programming to avoid recomputing subproblems."
            ),
            editorial = """
                ## Approach: Dynamic Programming
                
                We use bottom-up DP where dp[i] represents the minimum number of coins needed to make amount i.
                
                **Algorithm:**
                1. Initialize dp array of size amount + 1 with infinity
                2. Set dp[0] = 0 (base case)
                3. For each amount from 1 to target:
                   - For each coin:
                     - If coin <= amount:
                       - dp[amount] = min(dp[amount], dp[amount - coin] + 1)
                4. Return dp[amount] if it's not infinity, else -1
                
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
                TestCase(
                    input = "[1,2,5]\n11",
                    expectedOutput = "3",
                    isHidden = false
                ),
                TestCase(
                    input = "[2]\n3",
                    expectedOutput = "-1",
                    isHidden = false
                )
            )
        )

        // Insert all problems
        problemDao.insertProblem(twoSum)
        problemDao.insertProblem(containerWithMostWater)
        problemDao.insertProblem(validPalindrome)
        problemDao.insertProblem(longestSubstring)
        problemDao.insertProblem(maxDepth)
        problemDao.insertProblem(levelOrder)
        problemDao.insertProblem(numIslands)
        problemDao.insertProblem(validPath)
        problemDao.insertProblem(climbingStairs)
        problemDao.insertProblem(coinChange)
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

// Made with Bob
