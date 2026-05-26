# Week 09: Quiz

## Conceptual

1. **What is TensorFlow Lite?**
   A) Cloud service  B) Mobile ML library  C) Database

   Answer: _____

2. **TFLite models typically use what format?**

   Answer: _____

3. **True/False: TFLite requires internet connection.**

   Answer: _____

4. **Where to place .tflite model?**
   A) res/  B) assets/  C) database/

   Answer: _____

5. **What does Interpreter.run() do?**

   Answer: _______________________________________________

6. **Why normalize pixel values?**

   Answer (one sentence): _______________________________________________

7. **Typical TFLite inference time?**
   A) 1-5ms  B) 50-200ms  C) 1000-2000ms

   Answer: _____

8. **True/False: TFLite models are less accurate than full models.**

   Answer: _____

9. **What is quantization?**

   Answer: _______________________________________________

10. **Cloud vs offline - which is faster for small models?**

    Answer: _____

## Practical

11. **Add TFLite dependency:**
    ```gradle
    implementation 'org.tensorflow:tensorflow-_____:2.12.0'
    ```

    Answer: _____

12. **Complete:**
    ```java
    Interpreter tflite = new Interpreter(___________());
    ```

    Answer: _____

13. **What's wrong with this preprocessing?**
    ```java
    input[0][y][x][0] = (pixel >> 16) & 0xFF;  // Missing what?
    ```

    Answer: _______________________________________________

14. **How to run inference?**
    ```java
    tflite._____(input, output);
    ```

    Answer: _____

15. **Find predicted class:**
    ```java
    int predicted = _________(output[0]);
    ```

    Answer: _____

16. **True/False: Input must be exactly model's expected shape.**

    Answer: _____

17. **Measure latency:**
    ```java
    long start = System.currentTimeMillis();
    // inference
    long latency = System.currentTimeMillis() - _____;
    ```

    Answer: _____

18. **Test offline mode:**

    Answer (one action): _______________________________________________

19. **What if model file missing?**

    Answer: _______________________________________________

20. **Labels.txt format:**

    Answer: _______________________________________________

---

## Answers

1. B  2. .tflite  3. F  4. B  5. Runs inference on input  
6. Match training preprocessing  7. B  8. T (slightly)  
9. Reducing precision to int8  10. Offline (no network latency)  
11. lite  12. loadModelFile  13. / 255.0f normalization  
14. run  15. argmax  16. T  17. start  
18. Enable airplane mode  19. IOException/FileNotFoundException  
20. One label per line

---

**Score:** _____ / 20

**Pass:** ≥16
