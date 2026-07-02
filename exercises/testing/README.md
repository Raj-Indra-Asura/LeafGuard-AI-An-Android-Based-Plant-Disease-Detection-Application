# Testing and Quality Assurance Exercises

## Overview

This directory contains comprehensive testing exercises for the LeafGuard AI application. These exercises cover test case design, unit testing, integration testing, UI testing, edge case identification, debugging techniques, performance measurement, and optimization strategies.

## Weekly Mapping

### Week 11: Testing and Optimization
- Test case design and implementation
- Unit testing (ViewModel, Repository, Utils)
- Integration testing (Database, API)
- UI testing with Espresso
- Edge case identification and handling
- Debugging strategies and tools
- Performance profiling and optimization

## Exercise Categories

### 1. Test Case Design (Week 11)

**Exercise 1.1: Design Comprehensive Test Cases**

Create a test case matrix for the LeafGuard AI application:

```markdown
# Test Case Matrix

## Feature: Disease Detection

| Test ID | Test Case | Input | Expected Output | Priority | Type |
|---------|-----------|-------|-----------------|----------|------|
| TC-01 | Valid image upload | Tomato leaf image | Disease detected with confidence | High | Functional |
| TC-02 | Invalid image format | .txt file | Error: Invalid format | High | Validation |
| TC-03 | Very large image | 50MB image | Error: File too large | Medium | Validation |
| TC-04 | Empty image | 0 byte file | Error: Invalid image | Medium | Edge case |
| TC-05 | Low confidence result | Unclear image | Confidence < 50% warning | Medium | Functional |
| TC-06 | Multiple detections | Batch of 10 images | All processed correctly | Low | Performance |
| TC-07 | Network timeout | Delayed response | Timeout error handling | High | Error |
| TC-08 | No internet connection | Offline mode | Use cached model | High | Error |

## Feature: Database Operations

| Test ID | Test Case | Input | Expected Output | Priority | Type |
|---------|-----------|-------|-----------------|----------|------|
| DB-01 | Insert disease record | Valid disease data | Record saved successfully | High | Unit |
| DB-02 | Insert duplicate | Same disease twice | Both records saved | Medium | Unit |
| DB-03 | Query by ID | Valid disease ID | Correct disease returned | High | Unit |
| DB-04 | Query non-existent | Invalid ID | Null returned | Medium | Edge case |
| DB-05 | Update record | Modified disease data | Record updated | High | Unit |
| DB-06 | Delete record | Valid disease ID | Record removed | High | Unit |
| DB-07 | Query with filters | Search criteria | Filtered results | Medium | Integration |
| DB-08 | Database migration | Upgrade version | Data preserved | High | Integration |

## Feature: User Interface

| Test ID | Test Case | Input | Expected Output | Priority | Type |
|---------|-----------|-------|-----------------|----------|------|
| UI-01 | Launch app | App start | Main screen displays | High | UI |
| UI-02 | Navigate to history | Click history button | History screen shows | High | UI |
| UI-03 | Capture image | Click camera button | Camera opens | High | UI |
| UI-04 | Display results | Detection complete | Results screen shows | High | UI |
| UI-05 | Toggle favorite | Click star icon | Item marked favorite | Medium | UI |
| UI-06 | Search diseases | Enter search query | Filtered list displays | Medium | UI |
| UI-07 | Rotate device | Screen orientation change | Layout adjusts | Low | UI |
| UI-08 | Handle back press | Press back button | Previous screen shows | Medium | UI |
```

**Verification**:
- All critical features have test cases
- Edge cases are identified
- Priority levels are assigned
- Test types are categorized

**Exercise 1.2: Create Test Scenarios**

```kotlin
// Document test scenarios for complex workflows

/**
 * Test Scenario: Complete Detection Workflow
 *
 * Preconditions:
 * - App is installed and permissions granted
 * - Device has camera access
 * - Internet connection is available
 *
 * Steps:
 * 1. Launch application
 * 2. Click "Scan Plant" button
 * 3. Grant camera permission if requested
 * 4. Capture image of plant leaf
 * 5. Wait for processing
 * 6. View detection results
 * 7. Save to history
 * 8. Add notes to detection
 * 9. Mark as favorite
 * 10. Share results
 *
 * Expected Results:
 * - Camera opens successfully
 * - Image is captured
 * - Detection completes in < 3 seconds
 * - Results show disease name and confidence
 * - Record is saved to database
 * - Notes are persisted
 * - Favorite status is updated
 * - Share dialog appears
 *
 * Postconditions:
 * - Detection is visible in history
 * - Database contains new record
 * - Image is stored locally
 */
```

**Verification**:
- All scenarios are documented
- Steps are clear and reproducible
- Expected results are specific
- Edge cases are covered

### 2. Unit Testing (Week 11)

**Exercise 2.1: Test ViewModel**

```kotlin
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DiseaseViewModelTest {

    @Mock
    private lateinit var repository: PlantDiseaseRepository

    private lateinit var viewModel: DiseaseViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DiseaseViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insert disease should call repository insert`() = runTest {
        // Given
        val disease = PlantDisease(
            diseaseName = "Test Disease",
            plantType = "Test Plant",
            confidenceScore = 0.9f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/test/path"
        )
        `when`(repository.insert(disease)).thenReturn(1L)

        // When
        viewModel.insertDisease(disease)
        advanceUntilIdle()

        // Then
        verify(repository).insert(disease)
    }

    @Test
    fun `get all diseases should return flow from repository`() = runTest {
        // Given
        val diseases = listOf(
            PlantDisease(
                diseaseName = "Disease 1",
                plantType = "Plant 1",
                confidenceScore = 0.9f,
                detectionDate = System.currentTimeMillis(),
                imagePath = "/path1"
            )
        )
        `when`(repository.allDiseases).thenReturn(flowOf(diseases))

        // When
        val result = viewModel.allDiseases.first()

        // Then
        assertThat(result).isEqualTo(diseases)
        assertThat(result).hasSize(1)
    }

    @Test
    fun `delete disease should call repository delete`() = runTest {
        // Given
        val disease = PlantDisease(
            id = 1,
            diseaseName = "Test",
            plantType = "Test",
            confidenceScore = 0.9f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/test"
        )
        `when`(repository.delete(disease)).thenReturn(1)

        // When
        viewModel.deleteDisease(disease)
        advanceUntilIdle()

        // Then
        verify(repository).delete(disease)
    }

    @Test
    fun `toggle favorite should update status`() = runTest {
        // Given
        val diseaseId = 1
        val isFavorite = true
        `when`(repository.toggleFavorite(diseaseId, isFavorite)).thenReturn(1)

        // When
        viewModel.toggleFavorite(diseaseId, isFavorite)
        advanceUntilIdle()

        // Then
        verify(repository).toggleFavorite(diseaseId, isFavorite)
    }

    @Test
    fun `search diseases should return filtered results`() = runTest {
        // Given
        val query = "Blight"
        val diseases = listOf(
            PlantDisease(
                diseaseName = "Early Blight",
                plantType = "Tomato",
                confidenceScore = 0.9f,
                detectionDate = System.currentTimeMillis(),
                imagePath = "/path"
            )
        )
        `when`(repository.searchDiseases(query)).thenReturn(flowOf(diseases))

        // When
        val result = viewModel.searchDiseases(query).first()

        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].diseaseName).contains(query)
    }

    @Test
    fun `get statistics should return correct data`() = runTest {
        // Given
        val stats = DiseaseStatistics(
            totalCount = 10,
            favoriteCount = 3,
            averageConfidence = 0.85f,
            maxConfidence = 0.95f,
            minConfidence = 0.70f
        )
        `when`(repository.getStatistics()).thenReturn(stats)

        // When
        val result = viewModel.getStatistics()
        advanceUntilIdle()

        // Then
        assertThat(result).isEqualTo(stats)
        assertThat(result.totalCount).isEqualTo(10)
        assertThat(result.favoriteCount).isEqualTo(3)
    }
}
```

**Verification**:
```bash
./gradlew test
# Check test report in build/reports/tests/test/index.html
# Ensure all tests pass
# Verify code coverage is > 80%
```

**Exercise 2.2: Test Repository**

```kotlin
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlantDiseaseRepositoryTest {

    @Mock
    private lateinit var dao: PlantDiseaseDao

    private lateinit var repository: PlantDiseaseRepository

    @Before
    fun setup() {
        repository = PlantDiseaseRepository(dao)
    }

    @Test
    fun `insert should return row id`() = runTest {
        // Given
        val disease = createTestDisease()
        `when`(dao.insert(disease)).thenReturn(1L)

        // When
        val result = repository.insert(disease)

        // Then
        assertThat(result).isEqualTo(1L)
        verify(dao).insert(disease)
    }

    @Test
    fun `insertAll should return list of ids`() = runTest {
        // Given
        val diseases = listOf(createTestDisease(), createTestDisease())
        `when`(dao.insertAll(diseases)).thenReturn(listOf(1L, 2L))

        // When
        val result = repository.insertAll(diseases)

        // Then
        assertThat(result).hasSize(2)
        verify(dao).insertAll(diseases)
    }

    @Test
    fun `delete should return affected rows`() = runTest {
        // Given
        val disease = createTestDisease()
        `when`(dao.delete(disease)).thenReturn(1)

        // When
        val result = repository.delete(disease)

        // Then
        assertThat(result).isEqualTo(1)
        verify(dao).delete(disease)
    }

    @Test
    fun `getStatistics should aggregate data correctly`() = runTest {
        // Given
        `when`(dao.getCount()).thenReturn(10)
        `when`(dao.getFavoriteCount()).thenReturn(3)
        `when`(dao.getAverageConfidence()).thenReturn(0.85f)
        `when`(dao.getMaxConfidence()).thenReturn(0.95f)
        `when`(dao.getMinConfidence()).thenReturn(0.70f)

        // When
        val stats = repository.getStatistics()

        // Then
        assertThat(stats.totalCount).isEqualTo(10)
        assertThat(stats.favoriteCount).isEqualTo(3)
        assertThat(stats.averageConfidence).isWithin(0.01f).of(0.85f)
    }

    private fun createTestDisease() = PlantDisease(
        diseaseName = "Test Disease",
        plantType = "Test Plant",
        confidenceScore = 0.9f,
        detectionDate = System.currentTimeMillis(),
        imagePath = "/test/path"
    )
}
```

**Verification**:
- All repository methods are tested
- Mock interactions are verified
- Edge cases are covered

**Exercise 2.3: Test Utility Functions**

```kotlin
class ImageUtilsTest {

    @Test
    fun `resizeBitmap should maintain aspect ratio`() {
        // Given
        val original = Bitmap.createBitmap(1000, 500, Bitmap.Config.ARGB_8888)
        val targetSize = 224

        // When
        val resized = ImageUtils.resizeBitmap(original, targetSize)

        // Then
        assertThat(resized.width).isEqualTo(targetSize)
        assertThat(resized.height).isEqualTo(targetSize / 2)
    }

    @Test
    fun `normalizePixels should scale to -1 to 1`() {
        // Given
        val bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)

        // When
        val normalized = ImageUtils.normalizePixels(bitmap)

        // Then
        assertThat(normalized.min()).isAtLeast(-1.0f)
        assertThat(normalized.max()).isAtMost(1.0f)
    }

    @Test
    fun `formatConfidence should format as percentage`() {
        // Given
        val confidence = 0.8567f

        // When
        val formatted = FormatUtils.formatConfidence(confidence)

        // Then
        assertThat(formatted).isEqualTo("85.67%")
    }

    @Test
    fun `formatDate should format timestamp correctly`() {
        // Given
        val timestamp = 1609459200000L // 2021-01-01 00:00:00

        // When
        val formatted = FormatUtils.formatDate(timestamp)

        // Then
        assertThat(formatted).contains("2021")
        assertThat(formatted).contains("01")
    }
}
```

**Verification**:
- Utility functions behave correctly
- Edge cases are handled
- No exceptions are thrown

### 3. Integration Testing (Week 11)

**Exercise 3.1: Database Integration Tests**

```kotlin
@RunWith(AndroidJUnit4::class)
class PlantDiseaseDaoTest {

    private lateinit var database: LeafGuardDatabase
    private lateinit var dao: PlantDiseaseDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            LeafGuardDatabase::class.java
        ).build()
        dao = database.plantDiseaseDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndRetrieveDisease() = runBlocking {
        // Given
        val disease = PlantDisease(
            diseaseName = "Early Blight",
            plantType = "Tomato",
            confidenceScore = 0.95f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/test/path"
        )

        // When
        val id = dao.insert(disease)
        val retrieved = dao.getDiseaseById(id.toInt())

        // Then
        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.diseaseName).isEqualTo("Early Blight")
        assertThat(retrieved?.confidenceScore).isWithin(0.01f).of(0.95f)
    }

    @Test
    fun insertMultipleAndQueryAll() = runBlocking {
        // Given
        val diseases = listOf(
            createDisease("Disease 1", "Plant 1"),
            createDisease("Disease 2", "Plant 2"),
            createDisease("Disease 3", "Plant 3")
        )

        // When
        dao.insertAll(diseases)
        val all = dao.getAllDiseases().first()

        // Then
        assertThat(all).hasSize(3)
    }

    @Test
    fun updateDisease() = runBlocking {
        // Given
        val disease = createDisease("Original", "Plant")
        val id = dao.insert(disease)

        // When
        val updated = disease.copy(
            id = id.toInt(),
            diseaseName = "Updated"
        )
        dao.update(updated)
        val retrieved = dao.getDiseaseById(id.toInt())

        // Then
        assertThat(retrieved?.diseaseName).isEqualTo("Updated")
    }

    @Test
    fun deleteDisease() = runBlocking {
        // Given
        val disease = createDisease("Test", "Plant")
        val id = dao.insert(disease)

        // When
        dao.deleteById(id.toInt())
        val retrieved = dao.getDiseaseById(id.toInt())

        // Then
        assertThat(retrieved).isNull()
    }

    @Test
    fun searchByDiseaseName() = runBlocking {
        // Given
        dao.insertAll(listOf(
            createDisease("Early Blight", "Tomato"),
            createDisease("Late Blight", "Tomato"),
            createDisease("Powdery Mildew", "Cucumber")
        ))

        // When
        val results = dao.searchByDiseaseName("Blight").first()

        // Then
        assertThat(results).hasSize(2)
        assertThat(results.all { it.diseaseName.contains("Blight") }).isTrue()
    }

    @Test
    fun getStatistics() = runBlocking {
        // Given
        dao.insertAll(listOf(
            createDisease("Disease 1", "Plant", 0.9f),
            createDisease("Disease 2", "Plant", 0.8f),
            createDisease("Disease 3", "Plant", 0.7f)
        ))

        // When
        val avg = dao.getAverageConfidence()
        val max = dao.getMaxConfidence()
        val min = dao.getMinConfidence()
        val count = dao.getCount()

        // Then
        assertThat(count).isEqualTo(3)
        assertThat(avg).isWithin(0.01f).of(0.8f)
        assertThat(max).isWithin(0.01f).of(0.9f)
        assertThat(min).isWithin(0.01f).of(0.7f)
    }

    private fun createDisease(
        name: String,
        plant: String,
        confidence: Float = 0.9f
    ) = PlantDisease(
        diseaseName = name,
        plantType = plant,
        confidenceScore = confidence,
        detectionDate = System.currentTimeMillis(),
        imagePath = "/test/path"
    )
}
```

**Verification**:
```bash
./gradlew connectedAndroidTest
# Check test results
# Verify all database operations work correctly
```

**Exercise 3.2: API Integration Tests**

```kotlin
@RunWith(AndroidJUnit4::class)
class ApiIntegrationTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: LeafGuardApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(LeafGuardApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testSuccessfulDetection() = runBlocking {
        // Given
        val mockResponse = """
            {
                "predictions": [
                    {"disease": "Early Blight", "confidence": 0.95},
                    {"disease": "Late Blight", "confidence": 0.03},
                    {"disease": "Healthy", "confidence": 0.02}
                ]
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(mockResponse)
        )

        // When
        val response = apiService.detectDisease(createTestFile())

        // Then
        assertThat(response.predictions).hasSize(3)
        assertThat(response.predictions[0].disease).isEqualTo("Early Blight")
        assertThat(response.predictions[0].confidence).isWithin(0.01f).of(0.95f)
    }

    @Test
    fun testNetworkError() = runBlocking {
        // Given
        mockWebServer.enqueue(
            MockResponse().setResponseCode(500)
        )

        // When & Then
        assertThrows<HttpException> {
            apiService.detectDisease(createTestFile())
        }
    }

    private fun createTestFile(): MultipartBody.Part {
        val file = File.createTempFile("test", ".jpg")
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }
}
```

**Verification**:
- API calls work correctly
- Error responses are handled
- Mock server simulates real API

### 4. UI Testing with Espresso (Week 11)

**Exercise 4.1: Basic UI Tests**

```kotlin
@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAppLaunches() {
        onView(withId(R.id.toolbar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToHistory() {
        // Click history button
        onView(withId(R.id.btn_history))
            .perform(click())

        // Verify history screen is displayed
        onView(withId(R.id.history_recycler_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testCameraButtonClick() {
        onView(withId(R.id.btn_camera))
            .perform(click())

        // Verify camera permission dialog or camera opens
        // This depends on permission state
    }

    @Test
    fun testSearchFunctionality() {
        // Open search
        onView(withId(R.id.search_view))
            .perform(click())

        // Type query
        onView(withId(R.id.search_view))
            .perform(typeText("Blight"), closeSoftKeyboard())

        // Wait for results
        Thread.sleep(1000)

        // Verify results are filtered
        onView(withId(R.id.disease_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testDetailScreenDisplay() {
        // Click on first item in list
        onView(withId(R.id.disease_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        // Verify detail screen shows
        onView(withId(R.id.disease_detail_container))
            .check(matches(isDisplayed()))

        // Verify disease name is displayed
        onView(withId(R.id.tv_disease_name))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testFavoriteToggle() {
        // Click favorite button
        onView(withId(R.id.btn_favorite))
            .perform(click())

        // Verify state changes (icon or color)
        onView(withId(R.id.btn_favorite))
            .check(matches(isDisplayed()))
    }

    @Test
    fun testRotationPersistence() {
        // Enter text
        onView(withId(R.id.et_notes))
            .perform(typeText("Test notes"), closeSoftKeyboard())

        // Rotate device
        activityRule.scenario.onActivity { activity ->
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        Thread.sleep(500)

        // Verify text persists
        onView(withId(R.id.et_notes))
            .check(matches(withText("Test notes")))
    }
}
```

**Verification**:
```bash
./gradlew connectedAndroidTest
# Run on physical device or emulator
# Verify all UI interactions work correctly
```

### 5. Edge Case Identification (Week 11)

**Exercise 5.1: Document Edge Cases**

```kotlin
/**
 * Edge Cases for LeafGuard AI
 *
 * 1. Image Processing
 *    - Empty image file (0 bytes)
 *    - Corrupted image file
 *    - Very large image (> 50MB)
 *    - Very small image (< 10x10 pixels)
 *    - Non-image file with image extension
 *    - Black/white image
 *    - Extremely high/low brightness
 *
 * 2. Network
 *    - No internet connection
 *    - Slow connection (timeout)
 *    - Interrupted connection (mid-upload)
 *    - Server error (500)
 *    - Invalid API endpoint
 *
 * 3. Database
 *    - Maximum storage reached
 *    - Database corruption
 *    - Concurrent write operations
 *    - Very long text fields
 *    - Special characters in text
 *    - Null values
 *
 * 4. ML Model
 *    - Model file missing
 *    - Model file corrupted
 *    - Unsupported image format
 *    - Out of memory during inference
 *    - Unknown disease (not in training set)
 *    - Very low confidence (< 10%)
 *
 * 5. User Input
 *    - Empty fields
 *    - Maximum length exceeded
 *    - SQL injection attempts
 *    - XSS attempts
 *    - Invalid date ranges
 *
 * 6. Permissions
 *    - Camera permission denied
 *    - Storage permission denied
 *    - Location permission denied
 *    - Permissions revoked during use
 *
 * 7. Device States
 *    - Low memory
 *    - Low storage
 *    - Low battery
 *    - Airplane mode
 *    - Different screen sizes
 *    - Different Android versions
 */

class EdgeCaseTests {

    @Test
    fun testEmptyImageFile() {
        val emptyFile = File.createTempFile("empty", ".jpg")
        assertThrows<IllegalArgumentException> {
            ImageProcessor.processImage(emptyFile)
        }
    }

    @Test
    fun testVeryLargeImage() {
        // Create large bitmap
        val largeBitmap = Bitmap.createBitmap(10000, 10000, Bitmap.Config.ARGB_8888)

        // Should handle gracefully without crash
        val result = ImageProcessor.resizeIfNeeded(largeBitmap)
        assertThat(result.width).isAtMost(2048)
        assertThat(result.height).isAtMost(2048)
    }

    @Test
    fun testNullDatabaseValues() = runBlocking {
        val disease = PlantDisease(
            diseaseName = "Test",
            plantType = "Test",
            confidenceScore = 0.9f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/path",
            symptoms = null,  // Null optional field
            treatment = null,  // Null optional field
            notes = null
        )

        val id = dao.insert(disease)
        val retrieved = dao.getDiseaseById(id.toInt())

        assertThat(retrieved).isNotNull()
        assertThat(retrieved?.symptoms).isNull()
    }

    @Test
    fun testConcurrentDatabaseWrites() = runBlocking {
        val jobs = List(100) {
            launch {
                val disease = createTestDisease("Disease $it")
                dao.insert(disease)
            }
        }

        jobs.joinAll()

        val count = dao.getCount()
        assertThat(count).isEqualTo(100)
    }

    @Test
    fun testVeryLongText() = runBlocking {
        val longText = "A".repeat(10000)

        val disease = PlantDisease(
            diseaseName = longText,
            plantType = "Test",
            confidenceScore = 0.9f,
            detectionDate = System.currentTimeMillis(),
            imagePath = "/path"
        )

        val id = dao.insert(disease)
        val retrieved = dao.getDiseaseById(id.toInt())

        assertThat(retrieved?.diseaseName).hasLength(10000)
    }

    @Test
    fun testLowConfidenceScenario() {
        val lowConfidence = 0.15f

        val shouldWarn = ModelUtils.shouldWarnLowConfidence(lowConfidence)
        assertThat(shouldWarn).isTrue()

        val message = ModelUtils.getConfidenceMessage(lowConfidence)
        assertThat(message).contains("uncertain")
    }
}
```

**Verification**:
- All edge cases are documented
- Tests are implemented for critical cases
- App handles edge cases gracefully

### 6. Debugging Techniques (Week 11)

**Exercise 6.1: Implement Logging Strategy**

```kotlin
object AppLogger {
    private const val TAG = "LeafGuardAI"

    fun d(message: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun i(message: String, tag: String = TAG) {
        Log.i(tag, message)
    }

    fun w(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Log.w(tag, message, throwable)
    }

    fun e(message: String, throwable: Throwable? = null, tag: String = TAG) {
        Log.e(tag, message, throwable)
        // Send to crash reporting service in production
    }

    fun logDetection(diseaseName: String, confidence: Float, processingTime: Long) {
        d("Detection: $diseaseName (${confidence * 100}%) in ${processingTime}ms")
    }

    fun logDatabaseOperation(operation: String, duration: Long) {
        d("Database $operation completed in ${duration}ms")
    }

    fun logNetworkRequest(endpoint: String, responseCode: Int, duration: Long) {
        d("API $endpoint: $responseCode in ${duration}ms")
    }
}

// Usage in code
class DiseaseDetector {
    suspend fun detectDisease(bitmap: Bitmap): DetectionResult {
        val startTime = System.currentTimeMillis()

        AppLogger.d("Starting disease detection")

        try {
            val result = performDetection(bitmap)
            val duration = System.currentTimeMillis() - startTime

            AppLogger.logDetection(
                result.diseaseName,
                result.confidence,
                duration
            )

            return result
        } catch (e: Exception) {
            AppLogger.e("Detection failed", e)
            throw e
        }
    }
}
```

**Verification**:
- Logs appear in Logcat
- Different log levels are used appropriately
- Production logs don't expose sensitive data

**Exercise 6.2: Debug Network Issues**

```kotlin
class NetworkDebugInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        AppLogger.d("→ Request: ${request.method} ${request.url}")
        AppLogger.d("→ Headers: ${request.headers}")

        val startTime = System.currentTimeMillis()

        try {
            val response = chain.proceed(request)
            val duration = System.currentTimeMillis() - startTime

            AppLogger.d("← Response: ${response.code} in ${duration}ms")

            if (!response.isSuccessful) {
                AppLogger.w("Failed response: ${response.body?.string()}")
            }

            return response
        } catch (e: Exception) {
            AppLogger.e("Network error", e)
            throw e
        }
    }
}
```

**Verification**:
- Network requests are logged
- Response times are tracked
- Errors are captured with details

### 7. Performance Measurement (Week 11)

**Exercise 7.1: Measure Inference Time**

```kotlin
class PerformanceMonitor {
    private val measurements = mutableMapOf<String, MutableList<Long>>()

    fun measureInference(block: () -> Unit): Long {
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        val duration = (end - start) / 1_000_000 // Convert to ms

        addMeasurement("inference", duration)
        return duration
    }

    fun measureDatabaseOperation(operation: String, block: () -> Unit): Long {
        val start = System.nanoTime()
        block()
        val end = System.nanoTime()
        val duration = (end - start) / 1_000_000

        addMeasurement("db_$operation", duration)
        return duration
    }

    private fun addMeasurement(key: String, duration: Long) {
        measurements.getOrPut(key) { mutableListOf() }.add(duration)
    }

    fun getStatistics(key: String): Statistics? {
        val values = measurements[key] ?: return null

        return Statistics(
            count = values.size,
            average = values.average(),
            min = values.minOrNull() ?: 0L,
            max = values.maxOrNull() ?: 0L,
            median = values.sorted()[values.size / 2]
        )
    }

    fun printReport() {
        measurements.keys.forEach { key ->
            val stats = getStatistics(key)
            AppLogger.i("$key: avg=${stats?.average}ms, min=${stats?.min}ms, max=${stats?.max}ms")
        }
    }
}

data class Statistics(
    val count: Int,
    val average: Double,
    val min: Long,
    val max: Long,
    val median: Long
)
```

**Verification**:
```kotlin
@Test
fun testPerformanceMetrics() = runBlocking {
    val monitor = PerformanceMonitor()

    // Measure multiple inferences
    repeat(10) {
        monitor.measureInference {
            model.predict(testImage)
        }
    }

    // Get statistics
    val stats = monitor.getStatistics("inference")
    assertThat(stats?.average).isLessThan(500.0) // < 500ms average

    monitor.printReport()
}
```

**Exercise 7.2: Profile Memory Usage**

```kotlin
class MemoryProfiler {
    fun getMemoryInfo(): MemoryInfo {
        val runtime = Runtime.getRuntime()

        return MemoryInfo(
            totalMemory = runtime.totalMemory() / (1024 * 1024),
            freeMemory = runtime.freeMemory() / (1024 * 1024),
            maxMemory = runtime.maxMemory() / (1024 * 1024),
            usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)
        )
    }

    fun logMemoryUsage(tag: String) {
        val info = getMemoryInfo()
        AppLogger.d("[$tag] Memory: ${info.usedMemory}MB / ${info.maxMemory}MB")
    }
}

data class MemoryInfo(
    val totalMemory: Long,
    val freeMemory: Long,
    val maxMemory: Long,
    val usedMemory: Long
)
```

**Verification**:
- Memory usage is tracked
- Memory leaks are identified
- Optimization opportunities are found

## How to Complete Exercises

1. **Design test cases**: Create comprehensive test matrix
2. **Write unit tests**: Test individual components
3. **Implement integration tests**: Test component interactions
4. **Create UI tests**: Test user workflows
5. **Identify edge cases**: Document and test boundary conditions
6. **Add logging**: Implement debugging infrastructure
7. **Measure performance**: Profile and optimize
8. **Document findings**: Update reflection journal

## Testing Checklist

- [ ] Unit test coverage > 80%
- [ ] All critical features have tests
- [ ] Edge cases are identified and tested
- [ ] UI tests cover main workflows
- [ ] Performance meets requirements
- [ ] No memory leaks detected
- [ ] Error handling is tested
- [ ] Logging is comprehensive
- [ ] Debug tools are in place

## Common Issues and Solutions

**Issue**: Tests fail intermittently
**Solution**: Use IdlingResource for async operations, avoid Thread.sleep

**Issue**: UI tests fail on CI
**Solution**: Disable animations, use appropriate wait strategies

**Issue**: Memory leaks in tests
**Solution**: Properly clean up resources, use @After methods

## Resources

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Espresso Documentation](https://developer.android.com/training/testing/espresso)
- [JUnit 4 Guide](https://junit.org/junit4/)
- [Mockito Documentation](https://site.mockito.org/)
- [Android Profiler](https://developer.android.com/studio/profile/android-profiler)

## Submission Requirements

1. Complete test suite (unit, integration, UI)
2. Test coverage report
3. Edge case documentation
4. Performance benchmarks
5. Debug logs and analysis
6. Bug fixes implemented
7. Reflection journal for Week 11
