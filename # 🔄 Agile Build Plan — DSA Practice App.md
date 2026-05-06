# 🔄 Agile Build Plan — DSA Practice App (Kotlin Compose)

**Philosophy:** Every sprint ends with a **shippable, runnable app**. We build a thin vertical slice first, then expand. No phase leaves the app broken.

---

## 🧠 Agent Meta-Instructions (Read First, Always)

```
AGENT CORE RULES:
1. After EVERY sprint, the app must compile and run on a device/emulator.
2. Build vertically (one full feature end-to-end) not horizontally (all DAOs, then all ViewModels...).
3. Each sprint has a DEFINITION OF DONE — do not start next sprint until it passes.
4. Keep a WORKING_STATE.md file updated after every sprint with:
   - What is built and working
   - What is stubbed/mocked
   - What is next
5. When context gets long, re-read WORKING_STATE.md before continuing.
6. Never delete working code to add new code. Always extend.
7. If a sprint is too large, split it — never skip the Definition of Done.
```

---

## 🗂️ Sprint Map (12 Sprints)

| Sprint | Name | App State After |
|--------|------|-----------------|
| S1 | Skeleton + Theme | App opens, dark theme, bottom nav |
| S2 | First Problem Works | Can read + view 1 problem |
| S3 | Problem List | Browse + filter 10 problems |
| S4 | Code Editor | Type code, switch languages |
| S5 | Submission Flow | Submit, see results, XP |
| S6 | Home Dashboard | Streak, stats, daily challenge |
| S7 | Full Problem Bank | All 150 problems seeded |
| S8 | Analytics | Charts, heatmap, history |
| S9 | Streak & Gamification | Badges, freeze tokens, level |
| S10 | Focus Timer | Pomodoro timer, session log |
| S11 | Notifications + Widget | Reminders, home widget |
| S12 | Tablet + Polish | Adaptive layout, animations |

---

---

# SPRINT 1 — Skeleton + Theme
**App after this sprint:** Opens to a dark-themed app with bottom navigation and 5 empty placeholder screens.

---

## What to build

### 1. `build.gradle.kts` — ALL dependencies upfront
```
Add everything now so we never touch gradle again:
- Compose BOM (latest)
- Material3 + WindowSizeClass
- Navigation Compose
- Hilt + hilt-navigation-compose + hilt-work (KSP)
- Room + room-ktx (KSP)
- DataStore Preferences
- WorkManager
- Glance + glance-material3
- Google Fonts (ui-text-google-fonts)
- kotlinx-serialization-json
- Coroutines Android
```

### 2. Theme System (complete, final — never touch again)
```kotlin
// Color.kt — ALL colors defined now
Background=#0D1117, Surface=#161B22, SurfaceVariant=#21262D
Primary=#6366F1, PrimaryVariant=#818CF8
Secondary=#10B981 (Easy/Success)
Tertiary=#F59E0B (Medium/Warning)
Error=#F43F5E (Hard/Danger)
OnBackground=#E6EDF3, OnSurface=#C9D1D9, Muted=#8B949E
DifficultyEasy=#10B981, DifficultyMedium=#F59E0B, DifficultyHard=#F43F5E

// Type.kt
- Inter from Google Fonts → all UI text
- Fira Code from Google Fonts → code editor
- Define all TextStyles (displayLarge → labelSmall)

// Shape.kt — Small=8dp, Medium=12dp, Large=16dp, ExtraLarge=24dp
// Dimens.kt — spacing, icon sizes
// Theme.kt — DsaAppTheme composable, dark-first, status bar handling
```

### 3. Hilt Application
```kotlin
// DsaApp.kt
@HiltAndroidApp class DsaApp : Application()

// MainActivity.kt
@AndroidEntryPoint — sets content with DsaAppTheme
```

### 4. Navigation + Shell
```kotlin
// 5 routes: Home, Problems, Timer, Analytics, Settings
// NavHost with placeholder screens (Box + Text("Screen Name"))
// Bottom navigation bar component
// Each placeholder screen is its own file (will be filled later)
```

### 5. `WORKING_STATE.md` — create this file
```markdown
## Built & Working
- Theme system (dark mode, Inter + Fira Code fonts)
- Bottom navigation (5 tabs)
- All placeholder screens routable

## Stubbed / Mocked
- All screens show placeholder text

## Next Sprint
- S2: Room DB + single problem readable end-to-end
```

## ✅ Definition of Done — S1
```
□ App compiles and launches
□ Dark theme renders (#0D1117 background visible)
□ Bottom nav has 5 tappable tabs
□ Each tab shows its screen name
□ No crashes on any tab tap
□ WORKING_STATE.md created
```

---

---

# SPRINT 2 — First Problem Works End-to-End
**App after this sprint:** Navigate to Problems tab → see 1 hardcoded problem → tap it → read description + examples.

---

## What to build

### 1. Room Database — minimal schema (just what S2 needs)
```kotlin
// ProblemEntity.kt
@Entity("problems")
id: Int, title: String, description: String, difficulty: String,
topic: String, constraints: String, examplesJson: String,
hintsJson: String, editorial: String, timeEstimateMinutes: Int,
acceptanceRate: Float, xpReward: Int, starterCodeJson: String,
testCasesJson: String

// Converters.kt
data class Example(input: String, output: String, explanation: String?)
data class TestCase(input: String, expectedOutput: String, isHidden: Boolean)
List<Example> <-> String JSON
List<TestCase> <-> String JSON
Map<String,String> <-> String JSON

// ProblemDao.kt — only these queries now:
getAllProblems(): Flow<List<ProblemEntity>>
getProblemById(id: Int): Flow<ProblemEntity?>

// DsaDatabase.kt — version=1, just ProblemEntity for now
// DatabaseSeeder.kt — seed EXACTLY 1 problem: "Two Sum"
  Full data: real description, 2 examples, constraints, 2 hints,
  editorial, starter code in all 5 languages, 3 test cases
```

### 2. Repository + UseCase (thin slice only)
```kotlin
// ProblemRepository interface: getProblems(), getProblemById()
// ProblemRepositoryImpl: delegates to DAO, maps entity→domain model
// Problem.kt domain model (mirrors entity, no Room annotations)
// GetProblemsUseCase, GetProblemDetailUseCase
// DatabaseModule.kt (Hilt), RepositoryModule.kt (Hilt)
```

### 3. Problem List Screen — minimal
```kotlin
// ProblemListViewModel: UiState(problems: List<Problem>, isLoading: Boolean)
// ProblemListScreen: LazyColumn of simple cards
// ProblemCard (minimal): title + difficulty chip + topic tag + tap to navigate
```

### 4. Problem Detail Screen — read only
```kotlin
// ProblemDetailViewModel: UiState(problem: Problem?, isLoading: Boolean)
// ProblemDetailScreen layout:
//   - Title + DifficultyBadge + TopicTag
//   - Description text
//   - Examples (input/output styled blocks)  
//   - Constraints list
//   - Hints (collapsed, tap to reveal one by one)
//   - "Open Editor" button at bottom → navigate to editor (placeholder for now)
```

### 5. Navigation update
```kotlin
// Add ProblemDetail(problemId: Int) route
// ProblemList taps navigate to ProblemDetail
```

### 6. Update `WORKING_STATE.md`
```markdown
## Built & Working
- Theme, nav shell (S1)
- Room DB with ProblemEntity + seeder (1 problem)
- Problem list shows 1 card
- Problem detail shows full content for "Two Sum"

## Stubbed / Mocked
- Home, Timer, Analytics, Settings = placeholder
- Editor button on detail goes nowhere yet

## Next Sprint
- S3: Seed 10 problems, add filters + search
```

## ✅ Definition of Done — S2
```
□ Problems tab shows "Two Sum" card
□ Tapping it opens detail screen
□ Description, examples, constraints visible
□ Hints reveal on tap (one at a time)
□ Back button returns to list
□ No crashes
```

---

---

# SPRINT 3 — Problem List (10 Problems + Filters)
**App after this sprint:** Browse 10 problems, filter by topic/difficulty, search by name.

---

## What to build

### 1. Seed 10 problems (2 per topic)
```
Arrays: Two Sum (Easy), Container With Most Water (Medium)
Strings: Valid Palindrome (Easy), Longest Substring Without Repeating (Medium)
Trees: Maximum Depth of Binary Tree (Easy), Binary Tree Level Order Traversal (Medium)
Graphs: Number of Islands (Medium), Find if Path Exists (Easy)
DP: Climbing Stairs (Easy), Coin Change (Medium)

Each must have: full description, examples, constraints, hints,
editorial, starter code all 5 languages, test cases
```

### 2. Extend ProblemDao
```kotlin
// Add these queries:
getProblemsByTopic(topic: String): Flow<List<ProblemEntity>>
getProblemsByDifficulty(difficulty: String): Flow<List<ProblemEntity>>
searchProblems(query: String): Flow<List<ProblemEntity>>
getFilteredProblems(topic: String?, difficulty: String?): Flow<List<ProblemEntity>>
```

### 3. ProblemFilter domain model + GetProblemsUseCase update
```kotlin
data class ProblemFilter(
    val topic: String? = null,
    val difficulty: String? = null,
    val searchQuery: String = ""
)
// GetProblemsUseCase now accepts ProblemFilter
```

### 4. ProblemList Screen — full version
```kotlin
// ProblemListViewModel UiState:
data class ProblemListUiState(
    val problems: List<Problem>,
    val filter: ProblemFilter,
    val isSearchActive: Boolean,
    val isLoading: Boolean
)

// UI:
// - Search bar (animated expand/collapse)
// - Topic chips: All | Arrays | Strings | Trees | Graphs | DP
// - Difficulty chips: Easy | Medium | Hard (multiselect)
// - Stats row: "Showing X of 10"
// - LazyColumn of ProblemCards
// - Empty state when no results
```

### 5. DifficultyBadge + TopicTag components (reusable, final design)
```kotlin
// These will be used everywhere — build them properly now
// DifficultyBadge: pill shape, correct color per difficulty
// TopicTag: color-coded, with icon per topic
```

### 6. Update `WORKING_STATE.md`

## ✅ Definition of Done — S3
```
□ 10 problems visible in list
□ Topic filter chips work (tap Arrays → show only array problems)
□ Difficulty chips work (multiselect)
□ Search filters list in real-time
□ Empty state shows when no match
□ All 10 problems open in detail correctly
□ No crashes
```

---

---

# SPRINT 4 — Code Editor
**App after this sprint:** Open any problem → tap "Open Editor" → type code with syntax highlighting, switch languages, see starter template.

---

## What to build

### 1. UserSolutionEntity + DAO (minimal)
```kotlin
// UserSolutionEntity: id, problemId, language, code, status,
//   timeTakenSeconds, attemptCount, solvedAt, isFavorite, lastEditedAt
// UserSolutionDao: 
//   getSolutionForProblem(problemId): Flow<UserSolutionEntity?>
//   upsertSolution(solution)
// Add to DsaDatabase (VERSION STAYS 1 — we haven't shipped yet,
//   just add entity to @Database annotation)
```

### 2. SyntaxHighlighter
```kotlin
// File: ui/editor/SyntaxHighlighter.kt
// VisualTransformation for BasicTextField
// Highlight for all 5 languages:
//   - Keywords → Primary color (indigo)
//   - Strings → Secondary color (emerald)
//   - Comments → Muted color
//   - Numbers → Tertiary color (amber)
// Use regex, cache per-line to avoid recompute on every keystroke
```

### 3. AutoIndent
```kotlin
// File: ui/editor/AutoIndentTransformation.kt
// On Enter: copy current line indent
// After : or { → add one indent level
// Tab key → 4 spaces
```

### 4. CodeEditorScreen
```kotlin
// CodeEditorViewModel UiState:
data class CodeEditorUiState(
    val problem: Problem?,
    val preferredLanguage: Language, // from DataStore settings
    val currentCode: String,
    val timerSeconds: Long,
    val isTimerRunning: Boolean,
    val isDraftSaved: Boolean,
    val isLoading: Boolean
)

// Layout:
// TopBar: problem title + timer (stopwatch, running from open)
// Language indicator: Shows current language (read-only, set in Settings)
//   - Displays: "Python" | "C++" | "Java" | "C#" | "JavaScript"
//   - Tap → shows info: "Change language in Settings"
// Code editor area:
//   - Line numbers column (Canvas drawn)
//   - BasicTextField with SyntaxHighlighter VisualTransformation
//   - Fira Code font, 14sp default
//   - Scrollable horizontally + vertically
// Bottom action bar:
//   - Reset | Copy | Save | Export
//   - "Run" button (stub for now — shows "Coming in next update" snackbar)
// Auto-save: LaunchedEffect(code) { delay(30s); save draft }
// On open: Load template for preferred language from DataStore
```

### 4.1. Add Preferred Language to DataStore
```kotlin
// In UserPreferencesDataStore:
// Add key: PREFERRED_LANGUAGE (default: PYTHON)
// Add methods:
//   - getPreferredLanguage(): Flow<Language>
//   - setPreferredLanguage(language: Language)
```

### 5. CodeExporter
```kotlin
// File: ui/editor/CodeExporter.kt
// Write to internal storage → FileProvider → share sheet
// Add file header comment with problem title, difficulty, time
// Extension map: Python=.py, C++=.cpp, Java=.java, C#=.cs, JS=.js
```

### 6. Wire ProblemDetail → CodeEditor navigation

### 7. Update `WORKING_STATE.md`

## ✅ Definition of Done — S4
```
□ "Open Editor" on any problem opens CodeEditorScreen
□ Correct starter template loads for preferred language
□ Syntax highlighting colors keywords/strings/comments
□ Language indicator shows current language (read-only)
□ Tapping language indicator shows "Change in Settings" message
□ Timer counts up from 0:00
□ Line numbers display correctly
□ Copy button copies code to clipboard
□ Export opens share sheet with correct file
□ Auto-save indicator shows "Saved"
□ Back → ProblemDetail
```

---

---

# SPRINT 5 — Submission Flow + XP
**App after this sprint:** Submit code → see test case results → earn XP → solution saved.

---

## What to build

### 1. TestCaseRunner
```kotlin
// File: domain/executor/TestCaseRunner.kt
// Mock runner (no real execution sandbox):
// Input: code string + List<TestCase> + language
// Logic:
//   - Empty code → all fail
//   - No return statement → all fail
//   - For 10 seeded problems: hardcode correct solution signatures
//     that when present = pass (check for key function/method name)
//   - Simulate 200-800ms delay (kotlinx delay, not real execution)
//   - Return TestRunResult(results: List<TestCaseResult>, totalMs: Long)
// Add disclaimer: "Results are simulated. Run locally for exact output."
```

### 2. SubmitSolutionUseCase
```kotlin
// Takes: problemId, code, language, timeTakenSeconds
// Runs TestCaseRunner
// If any test passes → status = ATTEMPTED, if all pass → SOLVED
// Saves UserSolutionEntity
// Awards XP via PreferencesRepository (create DataStore now)
// Returns Flow<SubmissionResult>
```

### 3. DataStore — minimal (just XP/level now)
```kotlin
// UserPreferencesDataStore.kt
// Keys needed NOW: TOTAL_XP, CURRENT_LEVEL, ONBOARDING_COMPLETE, USER_NAME
// (Add more keys in later sprints as needed)
// XP logic: Easy=10, Medium=25, Hard=50
// Level thresholds: 0, 500, 1200, 2500, 5000...
// Return LevelUpEvent if threshold crossed
```

### 4. Submission UI in CodeEditorScreen
```kotlin
// Add to CodeEditorUiState:
val testCaseResults: List<TestCaseResult>?
val isRunning: Boolean
val submissionResult: SubmissionResult?
val xpGained: Int?
val isLevelUp: Boolean

// Add "Run & Submit" button (replace stub)
// Test Cases Panel (BottomSheet):
//   - Shows while running: progress animation
//   - Shows results: each test case pass ✅ / fail ❌
//   - Input + Expected + Actual per case
//   - "Simulated output" disclaimer

// Result overlay (full screen, appears on top):
//   - Success: green glow + "All Tests Passed! 🎉"
//   - Partial: amber + "X/Y Tests Passed"
//   - Fail: rose + "Tests Failed"
//   - XP gained: "+25 XP" animated counter
//   - Time taken display
//   - "Back to Problem" button
```

### 5. Confetti component
```kotlin
// File: ui/components/animations/ConfettiSystem.kt
// Canvas particle system, fires on full success
// 60 particles, theme colors, gravity + rotation, 2.5s duration
```

### 6. LevelUpOverlay component
```kotlin
// Full screen overlay, shows on level up
// "Level X!" with scale-in animation
// Auto-dismiss after 2.5s
```

### 7. Update `WORKING_STATE.md`

## ✅ Definition of Done — S5
```
□ "Run & Submit" button triggers test runner
□ Loading animation shows during run
□ Test case results show pass/fail per case
□ Solved status saved to DB
□ XP increments in DataStore
□ Success overlay + confetti shows on all pass
□ Level-up overlay shows when XP threshold crossed
□ Re-opening solved problem shows previous code
□ Problem card in list shows ✓ solved indicator
```

---

---

# SPRINT 6 — Home Dashboard
**App after this sprint:** Home tab shows live stats, streak, daily challenge, recent activity.

---

## What to build

### 1. Remaining DataStore keys
```kotlin
// Add to UserPreferencesDataStore:
DAILY_GOAL (default 3), IS_DARK_THEME (default true),
REMINDER_HOUR (default 20), REMINDER_MINUTE (default 0),
REMINDER_ENABLED (default true), CURRENT_STREAK (default 0),
BEST_STREAK (default 0), LAST_ACTIVE_DATE (""),
FREEZE_TOKENS (default 2), FONT_SIZE_PREFERENCE ("MEDIUM")
```

### 2. DailyProgressEntity + DAO
```kotlin
// DailyProgressEntity: date(PK), problemsSolved, goalTarget, xpEarned, streakActive
// DailyProgressDao: getProgressForDate(), upsertProgress(), getLast7Days()
// Add to DsaDatabase
```

### 3. Streak logic
```kotlin
// ProgressRepository:
//   - recordSolve(): increment today's DailyProgress
//     if first solve today → check if yesterday was active → increment streak
//     else start new streak from 1
//   - getCurrentStreak(): query consecutive streakActive days backwards
// Call recordSolve() from SubmitSolutionUseCase after successful solve
```

### 4. GetHomeStatsUseCase
```kotlin
// Combines: DataStore + DailyProgressDao + UserSolutionDao + ProblemDao
// Returns HomeStats:
data class HomeStats(
    val userName: String,
    val currentStreak: Int,
    val bestStreak: Int,
    val todaySolved: Int,
    val dailyGoal: Int,
    val totalXp: Int,
    val level: Int,
    val xpToNextLevel: Int,
    val dailyChallenge: Problem?,       // deterministic: problemId = dayOfYear % totalProblems
    val recentSolutions: List<UserSolution>, // last 5
    val topicProgresses: List<TopicProgress> // solved/total per topic
)
```

### 5. Canvas Components
```kotlin
// StreakRing.kt — circular progress Canvas component
//   Outer ring = daily goal, inner fill = solved today
//   Center: "X/Y", flame overlay when streak > 0
//   animateFloatAsState for smooth fill

// XpBar.kt — horizontal bar, level badge left, XP text right
//   animateFloatAsState for fill on XP change
```

### 6. HomeScreen — full implementation
```kotlin
// HomeViewModel: collects GetHomeStatsUseCase as StateFlow<HomeStats>

// Layout (LazyColumn):
// 1. TopBar: "Good [morning/afternoon/evening], {name}" + settings icon
// 2. Streak + XP row: StreakCounter + XpBar
// 3. Daily Challenge card (gradient border, elevated)
//    title + difficulty + "Solve Now" → ProblemDetail
// 4. Today's Progress: StreakRing + "X of Y problems"
// 5. Topic Progress: 5 rows with animated progress bars
// 6. Recent Activity: last 5 solved problems compact cards
// 7. Motivational quote (10 hardcoded quotes, rotate by day)
```

### 7. Minimal Onboarding (2 screens)
```kotlin
// Screen 1: Enter name + pick daily goal (slider)
// Screen 2: "You're ready!" with Get Started button
// Saves to DataStore, sets ONBOARDING_COMPLETE=true
// MainActivity: if !onboardingComplete → start at Onboarding route
```

### 8. Update `WORKING_STATE.md`

## ✅ Definition of Done — S6
```
□ Home tab shows real stats (not hardcoded)
□ Streak increments after solving a problem
□ Daily challenge shows a different problem each day
□ Recent activity shows last 5 solved problems
□ Topic progress bars reflect actual solve counts
□ XP bar shows correct level + progress
□ Onboarding shows on first launch
□ Name entered in onboarding appears in home greeting
□ No crashes on fresh install
```

---

---

# SPRINT 7 — Full Problem Bank (150 Problems)
**App after this sprint:** All 150 problems browsable with full content and all 5 language templates.

---

## What to build

### 1. Complete DatabaseSeeder — all 150 problems
```
Strategy: build seeder as a large Kotlin file with all problems defined
as data. Organize by topic. Each problem needs:
- Real title, description (3-5 sentences), difficulty, topic
- 2 examples with input/output/explanation
- 3-5 constraints  
- 2 hints (hint1=vague, hint2=more specific)
- Brief editorial (approach + time/space complexity)
- Starter code in Python, C++, Java, C#, JavaScript
- 3 test cases (2 visible, 1 hidden)
- timeEstimateMinutes, acceptanceRate, xpReward

Full list by topic:
ARRAYS (40): [as defined in original spec]
STRINGS (30): [as defined in original spec]  
TREES (26): [as defined in original spec]
GRAPHS (21): [as defined in original spec]
DP (33): [as defined in original spec]

Agent note: Write each problem fully. No "..." or "similar to above".
This is the most time-consuming step — take as many turns as needed.
Split into 5 separate files if needed:
  DatabaseSeeder_Arrays.kt
  DatabaseSeeder_Strings.kt
  DatabaseSeeder_Trees.kt
  DatabaseSeeder_Graphs.kt
  DatabaseSeeder_DP.kt
Then combine in DatabaseSeeder.kt
```

### 2. Extend ProblemDao with missing queries
```kotlin
// getFavoriteProblems(): Flow<List<ProblemEntity>>
// getProblemOfTheDay(seed: Int): problem at index (seed % total)
// getCountByTopic(topic): Int
// getCountByDifficulty(difficulty): Int
// getTotalCount(): Int
```

### 3. Update UserSolutionDao
```kotlin
// getSolvedCountByTopic(topic): Int
// getSolvedCountByDifficulty(difficulty): Int
// getSolutionsBetweenDates(start, end): Flow<List<UserSolutionEntity>>
// getFavorites(): Flow<List<UserSolutionEntity>>
// markFavorite(problemId, isFavorite)
```

### 4. Bookmarks Screen
```kotlin
// Reuses ProblemListScreen layout entirely
// Filtered to isFavorite = true
// Swipe-to-unfavorite (SwipeToDismiss)
// Empty state: "No bookmarks yet. Tap ♥ on any problem."
// Add bookmark toggle button to ProblemCard (animated ♥)
// Add bookmark toggle to ProblemDetail header
```

### 5. Update Problem List filters
```kotlin
// Add sort options: Default | Difficulty ↑ | Difficulty ↓ | Unsolved First | Fastest First
// Add bookmark filter chip
// Update stats row: "Solved: X/150"
// Pagination or keep all in memory (150 is fine for memory)
```

### 6. Update `WORKING_STATE.md`

## ✅ Definition of Done — S7
```
□ All 150 problems visible in list
□ Every problem has description, examples, constraints, hints
□ Every problem loads starter code in all 5 languages
□ Filters work across all 150 problems
□ Search works across all problem titles
□ Bookmark toggle saves and persists
□ Bookmarks tab shows favorited problems
□ Topic progress bars now show /correct totals (40/30/26/21/33)
□ Daily challenge rotates through all 150 problems
```

---

---

# SPRINT 8 — Analytics Screen
**App after this sprint:** Analytics tab shows heatmap, charts, and solve history.

---

## What to build

### 1. DailyProgressDao — extend
```kotlin
// getLast365Days(): Flow<List<DailyProgressEntity>>
// getProgressForDateRange(start, end): Flow<List<DailyProgressEntity>>
// getActiveDaysCount(): Int
// getLast12Weeks(): Flow<List<DailyProgressEntity>>
```

### 2. GetAnalyticsDataUseCase
```kotlin
// Returns AnalyticsData:
data class AnalyticsData(
    val heatmapData: List<HeatmapEntry>,    // date -> count, 365 days
    val weeklyData: List<WeeklyEntry>,       // week -> count, 12 weeks
    val topicDistribution: List<TopicCount>, // topic -> solved count
    val difficultyBreakdown: DifficultyBreakdown,
    val languageUsage: List<LanguageCount>,
    val currentStreak: Int,
    val bestStreak: Int,
    val totalSolved: Int,
    val accuracyRate: Float,                 // first-attempt / total
    val fastestSolves: Map<String, UserSolution?> // difficulty -> fastest
)
```

### 3. Canvas Chart Components
```kotlin
// HeatmapCalendar.kt
//   52×7 Canvas grid, cell=12dp gap=2dp
//   Color intensity: 0=surface, 1-4 = emerald shades
//   Month labels, day labels (M/W/F)
//   Tap cell → tooltip with date + count

// LineChart.kt
//   Bezier smooth curves (cubicTo)
//   Gradient fill below line
//   Animated path draw-in (PathMeasure)
//   X/Y axis labels

// DonutChart.kt
//   Animated arc sweep per segment
//   Center label, legend below

// BarChart.kt (horizontal)
//   Animated bar growth
//   Value labels, colored per category
```

### 4. AnalyticsScreen
```kotlin
// AnalyticsViewModel: StateFlow<AnalyticsUiState>

// Layout (LazyColumn):
// 1. Summary Cards (horizontal scroll):
//    Total Solved | Streak | Best Streak | Accuracy
// 2. Heatmap section: "Your Activity" + HeatmapCalendar
// 3. Weekly Progress: LineChart + time range chips (1M / 3M / All)
// 4. Topics: DonutChart + legend
// 5. Difficulty: 3 stat boxes with mini rings
// 6. Languages: horizontal BarChart
// 7. Personal Records:
//    Fastest Easy / Medium / Hard solves with problem name + time
```

### 5. Update `WORKING_STATE.md`

## ✅ Definition of Done — S8
```
□ Heatmap renders all 365 days (grey initially, colors on real data)
□ Line chart shows weekly progress (flat/0 initially = correct)
□ Donut chart shows topic distribution
□ Difficulty breakdown shows correct counts
□ Language usage updates after solving in different languages
□ Accuracy rate calculates correctly
□ Personal records show fastest solves
□ Tapping heatmap cell shows date tooltip
□ Time range chips filter the line chart
□ All charts animate on screen entry
```

---

---

# SPRINT 9 — Streak & Gamification
**App after this sprint:** Streak screen with calendar, badges, XP levels, freeze tokens all working.

---

## What to build

### 1. BadgeEntity + DAO + seeder
```kotlin
// BadgeEntity: id, name, description, iconType, isUnlocked, unlockedAt
// BadgeDao: getAllBadges(), unlockBadge(), getUnlockedBadges()
// Seed 12 badges in DatabaseSeeder (listed in original spec)
// Add to DsaDatabase
```

### 2. Badge unlock engine
```kotlin
// BadgeRepository + CheckAndAwardBadgesUseCase
// Triggers (call after each solve from SubmitSolutionUseCase):
//   STREAK_3/7/30/100/365: check currentStreak
//   SOLVED_10/50/100: check total solved count
//   SPEED_DEMON: Hard problem solved in < 600 seconds
//   POLYGLOT: check if all 5 languages used (from UserSolutionDao)
//   PERFECTIONIST: 10 consecutive first-attempt solves
//   NIGHT_OWL: System.currentTimeMillis() hour is 0-4
// Return list of newly unlocked badges → trigger celebration UI
```

### 3. MilestoneCelebration bottom sheet
```kotlin
// Slides up when badge unlocked
// Badge icon (Canvas drawn per iconType) + name + "Just Unlocked!"
// Dismiss button
// Queue system: if 2 badges unlocked same time, show one after other
```

### 4. Canvas Badge Icons
```kotlin
// File: ui/components/gamification/BadgeIconPainter.kt
// Canvas-drawn icons by iconType string:
//   FLAME → flame shape
//   TROPHY → trophy shape  
//   CLOCK → clock face
//   STAR → star polygon
//   OWL → simple owl face
//   GLOBE → circle with lines (globe)
//   LIGHTNING → lightning bolt
// Locked state: same icon, grayscale + 0.4f alpha + lock overlay
```

### 5. StreakScreen
```kotlin
// StreakViewModel: StateFlow<StreakUiState>

// Layout:
// 1. Hero: Giant flame (Canvas, pulsing gradient) + "🔥 X Day Streak"
//    "Best: Y days" subtitle
// 2. XP Card: Level badge + XpBar + "X XP to Level Y"
// 3. Monthly Calendar:
//    7×5 grid, colors: Complete=emerald, Missed=rose, Today=border, Future=surface
//    Month prev/next arrows
//    Tap day → show solved problems for that day (bottom sheet)
// 4. Freeze Tokens card:
//    Snowflake icons (X = remaining, greyed = used)
//    "Use Freeze Token" button (enabled only if today missed + tokens > 0)
// 5. Daily Goal slider (1-10, saves to DataStore)
// 6. Streak Milestones horizontal scroll:
//    3 | 7 | 14 | 30 | 60 | 100 | 200 | 365 day milestones
//    Locked/unlocked state
// 7. Badge Grid: all badges, locked=grayscale, tap=detail sheet
```

### 6. Freeze Token logic
```kotlin
// In ProgressRepository:
// useFreezeToken(): 
//   - Decrements FREEZE_TOKENS in DataStore
//   - Sets yesterday's DailyProgress.streakActive = true
//   - Keeps streak intact
// resetMonthlyFreezeTokens():
//   - Called on first open of new month
//   - Resets FREEZE_TOKENS to 2
```

### 7. Wire badge unlocks into submission flow
```kotlin
// SubmitSolutionUseCase: after save → call CheckAndAwardBadgesUseCase
// Pass result up through SubmissionResult
// CodeEditorScreen: show MilestoneCelebration for each new badge
```

### 8. Update `WORKING_STATE.md`

## ✅ Definition of Done — S9
```
□ Streak increments/resets correctly
□ Monthly calendar shows correct colors
□ Freeze token button works and persists
□ Freeze tokens reset to 2 on new month
□ All 12 badges visible in badge grid
□ Badges unlock at correct triggers
□ MilestoneCelebration shows on badge unlock
□ XP + level correct on streak screen
□ Daily goal slider saves and reflects on home screen
□ Solving a Hard problem in < 10min unlocks Speed Demon
```

---

---

# SPRINT 10 — Focus Timer
**App after this sprint:** Timer tab has working Pomodoro timer with session tracking.

---

## What to build

### 1. FocusSessionEntity + DAO
```kotlin
// FocusSessionEntity: id, linkedProblemId?, durationSeconds, sessionType, completedAt
// FocusSessionDao: insertSession(), getSessionsForDate(), getTotalFocusTime()
// Add to DsaDatabase
```

### 2. FocusTimerViewModel
```kotlin
// Uses ticker flow for countdown:
//   flow { while(true) { emit(Unit); delay(1000) } }
//   collectLatest in ViewModel
// UiState:
data class FocusTimerUiState(
    val timerState: TimerState, // IDLE/RUNNING/PAUSED/BREAK/COMPLETED
    val sessionType: SessionType, // QUICK_15/PRACTICE_25/DEEP_50/CUSTOM
    val totalSeconds: Long,
    val remainingSeconds: Long,
    val currentSession: Int,
    val targetSessions: Int,   // always 4
    val linkedProblem: Problem?,
    val todaySessions: List<FocusSession>,
    val totalMinutesToday: Long
)
// Save session to DB when: completed OR cancelled after > 5 minutes
```

### 3. Circular Timer Canvas Component
```kotlin
// File: ui/components/timer/CircularCountdown.kt
// Canvas arc: sweeps from -90° (top), clockwise
// Arc color: primary when running, amber when in break, muted when paused
// Background track: surface color
// Center: time remaining text (large, Fira Code font)
// Session dots below: ● for complete, ○ for remaining
// Animates smoothly (fraction = remaining/total, animateFloatAsState)
```

### 4. FocusTimerScreen
```kotlin
// Layout:
// 1. Session type chips: Quick 15m | Practice 25m | Deep Focus 50m | Custom
//    Custom → NumberPicker dialog (5-120 min)
// 2. Link Problem card:
//    "No problem linked" OR problem title
//    Tap → mini bottom sheet with problem search/select
// 3. Main circular timer (center, large)
// 4. Control buttons:
//    IDLE: Start (primary)
//    RUNNING: Pause + Stop
//    PAUSED: Resume + Stop
//    BREAK: Skip Break
// 5. Break screen overlay:
//    Breathing animation (Canvas expanding circle)
//    "Take a break! Resume in X:XX"
// 6. Today's Sessions list (bottom):
//    Each: sessionType + duration + timestamp
//    Total: "Xh Ym focused today"
```

### 5. Break + session cycle logic
```kotlin
// After PRACTICE_25 completes → 5min break → next session
// After 4 sessions → 15min long break
// Session counter resets at midnight
// Encouraging messages during break (10 hardcoded strings)
```

### 6. Update `WORKING_STATE.md`

## ✅ Definition of Done — S10
```
□ Timer counts down accurately (test with 1 min session)
□ Pause/resume works correctly
□ Break triggers automatically after session
□ Skip break works
□ Session dots update as sessions complete
□ Completed sessions save to DB
□ Today's sessions list shows correct data
□ Total focus time accumulates correctly
□ Linking a problem works
□ Custom duration picker works
□ Breathing animation plays during break
```

---

---

# SPRINT 11 — Notifications + Home Widget
**App after this sprint:** Daily reminders fire at set time, streak warnings fire near midnight, widget on home screen.

---

## What to build

### 1. Notification infrastructure
```kotlin
// AndroidManifest.xml additions:
//   POST_NOTIFICATIONS permission
//   RECEIVE_BOOT_COMPLETED permission
//   BootReceiver BroadcastReceiver
//   DsaWidgetReceiver AppWidgetProvider
//   AppWidgetProviderInfo XML

// NotificationChannels.kt (create in DsaApp.onCreate):
//   daily_reminder (HIGH), streak_warning (HIGH), milestones (DEFAULT)

// DsaNotificationManager.kt:
//   showDailyReminderNotification(problemTitle)
//   showStreakWarningNotification(streak, hoursLeft)
//   showWeeklySummaryNotification(solved, streak)
//   showMilestoneNotification(badgeName)
//   All use PendingIntent with correct deep links
//   All handle Android 13+ permission check
```

### 2. WorkManager Workers
```kotlin
// DailyReminderWorker (@HiltWorker):
//   Check reminder enabled → check today's progress
//   If not solved → get daily challenge title → fire notification
//   Schedule: PERIODIC 24h with initial delay to hit user's set time

// StreakGuardWorker (@HiltWorker):
//   Run every hour, check if within 2h of midnight
//   If daily goal not met AND streak > 0 → fire streak warning
//   Schedule: PERIODIC 1h, KEEP policy

// WeeklySummaryWorker (@HiltWorker):
//   Check if Sunday + between 8-9PM
//   Query last 7 days stats → fire summary notification
//   Schedule: PERIODIC 24h

// BootReceiver.kt:
//   On BOOT_COMPLETED → reschedule all workers

// WorkerScheduler.kt (singleton):
//   scheduleAll() — called after onboarding complete
//   rescheduleReminder(hour, minute)
//   cancelAll()
```

### 3. Settings Screen — notification section
```kotlin
// SettingsScreen (replaces placeholder):

// Section: Code Editor
//   Preferred Language dropdown: Python | C++ | Java | C# | JavaScript
//     - Saves to DataStore
//     - Shows info: "Applies to all problems"
//   Editor font size: Small / Medium / Large

// Section: Notifications
//   Master toggle
//   Daily reminder toggle + time picker (hour/minute)
//   Streak warning toggle
//   Weekly summary toggle
//   On any change → WorkerScheduler.reschedule*()

// Section: Appearance
//   Theme toggle: Dark / Light

// Section: Goals
//   Daily goal (redirects to Streak screen slider)

// Section: Data
//   Reset All Progress (double-confirm dialog)
//   App version

// Section: Profile
//   Edit name field
```

### 4. Glance Widget
```kotlin
// DsaWidget.kt (GlanceAppWidget):
// Layout:
//   App icon + "DSA Practice"
//   "🔥 X Day Streak"
//   "Today: X/Y"
//   Divider
//   Daily challenge title (truncated 30 chars) + difficulty chip
//   "Solve Now" button → deep link

// DsaWidgetReceiver.kt (GlanceAppWidgetReceiver)
// res/xml/dsa_widget_info.xml (minWidth=250dp, minHeight=110dp)

// Widget data updates:
//   Call GlanceAppWidgetManager.update() after:
//     - Problem solved (in SubmitSolutionUseCase)
//     - App comes to foreground (MainActivity.onResume)
```

### 5. Deep links
```kotlin
// AndroidManifest: intent-filter for problem://id/{problemId}
// NavGraph: deepLinks = listOf(navDeepLink { uriPattern = "problem://id/{problemId}" })
// MainActivity: handle deep link from notification PendingIntent
```

### 6. Update `WORKING_STATE.md`

## ✅ Definition of Done — S11
```
□ Request notification permission on first launch (Android 13+)
□ Daily reminder fires (test by setting reminder 2 min from now)
□ Streak warning fires when goal not met near midnight (test with mock time)
□ Weekly summary fires on Sunday (test with one-shot worker trigger)
□ Boot receiver reschedules workers after restart
□ Widget appears in system widget picker
□ Widget shows correct streak + today's progress
□ Widget "Solve Now" opens correct problem
□ Notification tap opens correct screen
□ Settings notification toggles enable/disable workers
□ Reminder time change reschedules worker correctly
```

---

---

# SPRINT 12 — Tablet Layout + Final Polish
**App after this sprint:** Full adaptive layout on tablets, smooth animations, production-ready.

---

## What to build

### 1. Adaptive Layout System
```kotlin
// AdaptiveScaffold.kt using WindowSizeClass:
// COMPACT (<600dp):
//   BottomNavigationBar, single pane all screens
// MEDIUM (600-840dp):
//   NavigationRail (no labels), single pane
// EXPANDED (>840dp):
//   NavigationRail (with labels)
//   ListDetailPaneScaffold on Problems (list + detail side by side)
//   SplitPane on CodeEditor (problem left, editor right)
//   Two-column grid on Analytics charts
//   Two-column grid on Home topic progress

// DsaNavRail.kt (mirrors BottomNav but vertical)
// SplitPane.kt (for ProblemDetail + CodeEditor on tablet)
// ListDetailPaneScaffold integration in ProblemListScreen
```

### 2. Animation Polish Pass
```kotlin
// Motion.kt: define SpringSpec, TweenSpec, enter/exit transitions

// Apply to:
// - NavHost screen transitions: fadeIn+slideIn / fadeOut
// - ProblemCard list: staggered appear (index * 30ms delay)
// - Filter chip select: scale + color fill animation
// - BottomSheet: spring slide up
// - Tab switching: animated indicator slide
// - Heatmap: cells fade in row by row (staggered)
// - Line chart: path draw-in animation (PathMeasure)
// - Streak ring: animateFloatAsState on first draw
// - XP bar: animateFloatAsState on XP gain
// - Submission result overlay: scale + fade in
// - Badge unlock: glow pulse on icon
// - Timer arc: smooth animateFloatAsState countdown
```

### 3. Edge Cases
```kotlin
// Empty states (all screens):
//   Problems: "No problems match your filters" + clear filters button
//   Bookmarks: "No bookmarks yet" + illustration
//   Analytics: "Solve some problems to see stats here"
//   Streak: "Start your streak today!" CTA

// Back gesture handling:
//   CodeEditor with unsaved changes → confirm dialog "Discard changes?"
//   BackHandler for modal bottom sheets

// Keyboard handling:
//   WindowInsets.ime on CodeEditor so keyboard doesn't cover input
//   ime padding on all scrollable screens

// Permission handling:
//   Notifications denied → graceful fallback, show in-app banner
//   Show "Enable in Settings" button if permanently denied
```

### 4. Performance
```kotlin
// Syntax highlighter: cache highlighted spans per line
//   key = (lineContent, language) → AnnotatedString
//   Use remember(lineContent, language) { highlight(lineContent) }

// ProblemList: use key { problem.id } in LazyColumn items
// HeatmapCalendar: draw all cells in single Canvas pass
// All DB queries confirmed on Dispatchers.IO
// ViewModels: use distinctUntilChanged() on flows to prevent unnecessary recomposition
```

### 5. Final WORKING_STATE.md update
```markdown
## App Complete — All Features Working

### Core Features
- 150 DSA problems (Arrays/Strings/Trees/Graphs/DP)
- Code editor with syntax highlighting (5 languages)
- Test case runner with submission flow
- XP system + levels

### Analytics & Gamification  
- GitHub-style heatmap (365 days)
- Weekly/topic/language/difficulty charts
- 12 unlockable badges
- Streak system with freeze tokens
- Pomodoro focus timer

### Platform Features
- Notifications (daily reminder, streak guard, weekly summary)
- Home screen widget
- Adaptive layout (phone + tablet)
- Onboarding flow
- Full settings screen

### Architecture
- Clean Architecture + MVI
- Room DB (offline-first)
- Hilt DI
- WorkManager for background tasks
- DataStore for preferences
- Glance for widget
```

## ✅ Definition of Done — S12 (Final)
```
TABLET:
□ NavigationRail shows on medium/expanded screens
□ Problem list + detail split pane works on tablet
□ Code editor split pane works on tablet
□ Analytics two-column layout on tablet
□ No UI clipping or overflow on any screen size

ANIMATIONS:
□ Screen transitions smooth (no flicker)
□ Problem list cards stagger on load
□ Charts animate on first render
□ XP bar animates on gain
□ All bottom sheets spring up smoothly

EDGE CASES:
□ Empty states on all screens
□ Keyboard doesn't cover editor
□ Back gesture shows discard dialog in editor
□ Notification permission denied handled gracefully
□ Fresh install → onboarding → home with 0 data looks correct

PERFORMANCE:
□ Scroll 150 problems without jank
□ Type in editor without lag
□ App cold start under 2 seconds
□ No ANRs or crashes in normal use flow
```

---

---

# 🤖 Agent Execution Script (Paste at Start of Each Sprint)

```
You are building a DSA practice app in Kotlin Jetpack Compose.

CURRENT SESSION RULES:
1. Read WORKING_STATE.md first to understand what is already built.
2. You are working on Sprint [NUMBER]: [NAME].
3. The app must compile and run at the END of this sprint.
4. Build vertically: complete one feature end-to-end before starting the next.
5. Do not rewrite existing working code — only extend it.
6. Every new class that needs injection must be added to its Hilt module immediately.
7. Every new Entity must be added to @Database annotation immediately.
8. After completing the sprint, update WORKING_STATE.md.
9. Run the Definition of Done checklist before declaring the sprint complete.

If you are running low on context:
- Stop at the nearest compile point
- Update WORKING_STATE.md with exactly what is done and what remains
- The next session will read WORKING_STATE.md and continue

Begin Sprint [NUMBER].
```