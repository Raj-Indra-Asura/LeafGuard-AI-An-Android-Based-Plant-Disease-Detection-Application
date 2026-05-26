# Week 08: Quiz

## Conceptual (10 questions)

1. **What is XML?**
   A) Database  B) Markup language  C) Programming language

   Answer: _____

2. **True/False: XML supports comments.**

   Answer: _____

3. **Which parser does Android recommend?**
   A) DOM  B) SAX  C) XmlPullParser

   Answer: _____

4. **Where do you store disease_library.xml?**
   A) res/  B) assets/  C) database/

   Answer: _____

5. **What does XmlPullParser.START_TAG indicate?**

   Answer: _______________________________________________

6. **True/False: Assets folder files are read-only.**

   Answer: _____

7. **Why use XML for disease library?**

   Answer (one sentence): _______________________________________________

8. **What's the root element in disease_library.xml?**

   Answer: _____

9. **How to open file from assets?**
   A) getAssets().open()  B) openFile()  C) readAsset()

   Answer: _____

10. **True/False: XML parsing can throw exceptions.**

    Answer: _____

---

## Practical (10 questions)

11. **Complete: `<disease>...</_____>`**

    Answer: _____

12. **What's wrong?**
    ```xml
    <disease>
        <label>Disease
    </disease>
    ```

    Answer: _______________________________________________

13. **How to get tag name in XmlPullParser?**

    Answer: _______________________________________________

14. **Complete:**
    ```java
    InputStream is = getAssets().___("disease_library.xml");
    ```

    Answer: _____

15. **What does parser.next() do?**

    Answer: _______________________________________________

16. **How to find disease by label?**

    Answer (one sentence): _______________________________________________

17. **What exception can parsing throw?**

    Answer: _____

18. **True/False: XmlPullParser loads entire XML to memory.**

    Answer: _____

19. **How to extract text content?**
    ```java
    String text = parser._________();
    ```

    Answer: _____

20. **What if XML file missing?**

    Answer: _______________________________________________

---

## Answers

1. B  2. T  3. C  4. B  5. Opening tag encountered  
6. T  7. CSE 2206 requirement + structured storage  8. diseases  
9. A  10. T  11. disease  12. Missing closing label tag  
13. parser.getName()  14. open  15. Moves to next event  
16. Loop through list, match label field  17. XmlPullParserException, IOException  
18. F  19. getText()  20. IOException thrown

---

**Score:** _____ / 20

**Pass:** ≥16
