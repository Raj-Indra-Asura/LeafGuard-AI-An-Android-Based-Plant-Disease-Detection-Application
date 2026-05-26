# Android Development Exercises

## Overview

This directory contains hands-on Android development exercises for the LeafGuard AI project. These exercises are designed to build your practical skills in Android app development, progressing from basic UI components to advanced features like database integration and machine learning model deployment.

## Weekly Mapping

### Weeks 2-3: Android Fundamentals
- Basic UI components and layouts
- Activity lifecycle
- Intent navigation
- User input handling

### Weeks 7-11: Advanced Android Features
- Week 7: Room Database integration
- Week 8: API integration and networking
- Week 9: TensorFlow Lite model integration
- Week 10: Camera integration and image processing
- Week 11: Testing, debugging, and optimization

## Exercise Categories

### 1. UI and Layouts (Weeks 2-3)

**Exercise 1.1: Create Plant List Layout**
- Design a RecyclerView to display a list of plant diseases
- Implement CardView for each list item
- Add icons and text styling
- **Verify**: Run the app, check if the list scrolls smoothly and displays all items

**Exercise 1.2: Design Disease Detail Screen**
- Create a detailed view showing disease information
- Use ConstraintLayout for responsive design
- Add ImageView for disease photos
- Include TextViews for symptoms and treatment
- **Verify**: Navigate to detail screen, verify all elements are properly aligned

**Exercise 1.3: Build Camera Capture Interface**
- Design a custom camera preview layout
- Add capture button with appropriate styling
- Implement gallery selection option
- **Verify**: Test on device, ensure camera preview is visible and buttons are functional

### 2. Intents and Navigation (Weeks 2-3)

**Exercise 2.1: Implement Activity Navigation**
- Create intents to navigate between MainActivity and DetailActivity
- Pass plant disease data between activities
- Implement back navigation
- **Verify**: Navigate through all screens, verify data is passed correctly

**Exercise 2.2: Implicit Intents**
- Implement share functionality for disease information
- Add email intent for sending reports
- Create gallery picker intent
- **Verify**: Test share dialog, email composition, and gallery selection

**Exercise 2.3: Result Handling**
- Implement camera capture with result callback
- Handle gallery selection results
- Process and display selected images
- **Verify**: Capture/select image and verify it displays in your app

### 3. Permissions (Weeks 7-8)

**Exercise 3.1: Runtime Permission Handling**
- Implement camera permission request
- Add storage permission handling
- Create permission rationale dialogs
- **Verify**: Deny and grant permissions, verify app handles both cases gracefully

**Exercise 3.2: Permission Best Practices**
- Check permissions before accessing features
- Handle permission denial scenarios
- Implement fallback options
- **Verify**: Test with different permission states, ensure no crashes

### 4. Room Database (Week 7)

**Exercise 4.1: Define Entities**
```kotlin
@Entity(tableName = "plant_diseases")
data class PlantDisease(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "disease_name") val diseaseName: String,
    @ColumnInfo(name = "plant_type") val plantType: String,
    @ColumnInfo(name = "confidence_score") val confidenceScore: Float,
    @ColumnInfo(name = "detection_date") val detectionDate: Long,
    @ColumnInfo(name = "image_path") val imagePath: String
)
```
- Create PlantDisease entity
- Add appropriate annotations
- Define primary keys and column names
- **Verify**: Build project successfully, check generated SQL schema

**Exercise 4.2: Create DAO Interface**
```kotlin
@Dao
interface PlantDiseaseDao {
    @Insert
    suspend fun insert(disease: PlantDisease): Long

    @Query("SELECT * FROM plant_diseases ORDER BY detection_date DESC")
    fun getAllDiseases(): Flow<List<PlantDisease>>

    @Query("SELECT * FROM plant_diseases WHERE id = :diseaseId")
    suspend fun getDiseaseById(diseaseId: Int): PlantDisease?

    @Delete
    suspend fun delete(disease: PlantDisease)
}
```
- Implement all CRUD operations
- Use Flow for observable queries
- Add custom queries for filtering
- **Verify**: Test each DAO method in isolation

**Exercise 4.3: Database Implementation**
```kotlin
@Database(entities = [PlantDisease::class], version = 1)
abstract class LeafGuardDatabase : RoomDatabase() {
    abstract fun plantDiseaseDao(): PlantDiseaseDao

    companion object {
        @Volatile
        private var INSTANCE: LeafGuardDatabase? = null

        fun getDatabase(context: Context): LeafGuardDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LeafGuardDatabase::class.java,
                    "leafguard_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```
- Create database class
- Implement singleton pattern
- Handle database migrations
- **Verify**: Insert test data, query it back, verify persistence across app restarts

**Exercise 4.4: Repository Pattern**
- Create repository to abstract data access
- Implement data operations using coroutines
- Add error handling
- **Verify**: Test repository methods from ViewModel

### 5. TensorFlow Lite Integration (Week 9)

**Exercise 5.1: Load TFLite Model**
```kotlin
class DiseaseClassifier(context: Context) {
    private var interpreter: Interpreter? = null

    init {
        val model = loadModelFile(context)
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(context: Context): ByteBuffer {
        val fileDescriptor = context.assets.openFd("plant_disease_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}
```
- Load model from assets
- Initialize TFLite interpreter
- Handle loading errors
- **Verify**: Check logs for successful model loading, verify model file exists

**Exercise 5.2: Image Preprocessing**
```kotlin
fun preprocessImage(bitmap: Bitmap): ByteBuffer {
    val inputSize = 224
    val byteBuffer = ByteBuffer.allocateDirect(4 * inputSize * inputSize * 3)
    byteBuffer.order(ByteOrder.nativeOrder())

    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)
    val intValues = IntArray(inputSize * inputSize)
    scaledBitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)

    var pixel = 0
    for (i in 0 until inputSize) {
        for (j in 0 until inputSize) {
            val value = intValues[pixel++]
            byteBuffer.putFloat(((value shr 16 and 0xFF) - 127.5f) / 127.5f)
            byteBuffer.putFloat(((value shr 8 and 0xFF) - 127.5f) / 127.5f)
            byteBuffer.putFloat(((value and 0xFF) - 127.5f) / 127.5f)
        }
    }
    return byteBuffer
}
```
- Resize image to model input size
- Normalize pixel values
- Convert to ByteBuffer format
- **Verify**: Log input tensor values, verify they're in expected range

**Exercise 5.3: Run Inference**
```kotlin
fun classifyImage(bitmap: Bitmap): List<Classification> {
    val input = preprocessImage(bitmap)
    val output = Array(1) { FloatArray(NUM_CLASSES) }

    interpreter?.run(input, output)

    return output[0].mapIndexed { index, confidence ->
        Classification(CLASS_LABELS[index], confidence)
    }.sortedByDescending { it.confidence }
}
```
- Execute model inference
- Process output probabilities
- Return top predictions
- **Verify**: Test with sample images, verify predictions make sense

**Exercise 5.4: Display Results**
- Show top 3 predictions in UI
- Display confidence scores as percentages
- Add visual indicators for confidence levels
- **Verify**: Run inference on multiple images, verify UI updates correctly

## How to Complete Exercises

1. **Read the Exercise Description**: Understand what you need to build
2. **Review Week Materials**: Refer to syllabus and lecture notes
3. **Write the Code**: Implement the solution incrementally
4. **Test Thoroughly**: Use verification steps to validate your work
5. **Document Your Solution**: Add comments explaining your approach
6. **Reflect**: Update your weekly reflection journal

## Verification Guidelines

### Build Verification
```bash
./gradlew clean build
```
- Ensure no compilation errors
- Check warnings and resolve them

### Runtime Verification
1. Run on emulator or physical device
2. Test all user interactions
3. Check logcat for errors
4. Verify expected behavior

### Code Quality Checks
```bash
./gradlew lint
```
- Review lint warnings
- Fix critical issues
- Follow Android best practices

## Common Issues and Solutions

### Issue: App crashes on startup
**Solution**: Check logcat for stack trace, verify all dependencies are properly initialized

### Issue: Database not persisting data
**Solution**: Ensure database operations are on background thread, check Room configuration

### Issue: Camera preview not showing
**Solution**: Verify camera permissions are granted, check if camera hardware is available

### Issue: TFLite model not loading
**Solution**: Verify model file is in assets folder, check file path spelling

### Issue: Out of memory errors
**Solution**: Optimize bitmap loading, use appropriate image sizes, implement proper cleanup

## Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)
- [TensorFlow Lite Android Guide](https://www.tensorflow.org/lite/android)
- [Material Design Guidelines](https://material.io/design)

## Getting Help

1. Review error messages carefully
2. Check Stack Overflow for similar issues
3. Consult Android documentation
4. Ask questions in discussion forum
5. Review completed exercises from previous weeks

## Assessment Criteria

Each exercise will be evaluated based on:
- **Functionality** (40%): Does it work as specified?
- **Code Quality** (30%): Is the code clean and well-organized?
- **Best Practices** (20%): Does it follow Android conventions?
- **Documentation** (10%): Are comments and explanations clear?

## Submission

1. Ensure all code compiles without errors
2. Test on at least one device/emulator
3. Add comments explaining key sections
4. Update your reflection journal
5. Commit code with descriptive messages
6. Push to your repository branch
