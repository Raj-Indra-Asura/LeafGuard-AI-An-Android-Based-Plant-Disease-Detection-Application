# Week 06 Reflection

After completing Week 06's machine learning model integration, take time to reflect on your experience. Write thoughtful responses to the prompts below. Aim for 2-3 paragraphs per section, totaling 500-800 words overall. This reflection helps solidify your learning and identify areas for deeper study.

---

## 1. Integration vs Implementation: Engineering Perspective

This week, you integrated a pre-trained machine learning model rather than building features from scratch (like UI layouts or API endpoints in previous weeks). Reflect on how this integration work differed from implementation work.

**Reflection prompts**:
- What was different about integrating an existing model versus implementing features from scratch?
- What new types of debugging challenges did you encounter? How did they differ from debugging UI or networking code?
- When working with the model as a "black box," what strategies did you use to understand its behavior?
- What documentation or resources were most helpful when dealing with the model's input/output contracts?
- How did testing change when working with ML components versus traditional software components?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 2. Confidence vs Accuracy: Communicating Uncertainty

You implemented confidence scores, but learned they don't guarantee prediction accuracy. Models can be confidently wrong, especially for out-of-distribution inputs.

**Reflection prompts**:
- How would you explain the difference between confidence and accuracy to a non-technical user (e.g., a farmer using your app)?
- What UI/UX choices did you make (or would you make) to communicate uncertainty appropriately?
- Did your testing reveal cases where the model had high confidence but you suspected the prediction was wrong? What did you observe?
- How do you balance providing helpful predictions with being honest about limitations?
- If you were to add a "Report Incorrect Prediction" feature, how would you design it? What data would you collect?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 3. Model as Black Box: When to Look Inside

Throughout Week 06, you treated the ML model as a black box, focusing only on input/output contracts. This is appropriate for most mobile engineering work, but sometimes deeper understanding is needed.

**Reflection prompts**:
- When might you need to understand model internals (architecture, training process) rather than just the API?
- What problems this week could be solved with black-box integration? What problems would require deeper ML knowledge?
- If you used a 6-class fallback model instead of 38-class, how did you handle this limitation? How did you communicate it to users?
- When is black-box integration sufficient for production systems? When is it insufficient?
- How would you decide whether to invest time learning ML deeply versus focusing on integration skills?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 4. Handling Limitations: Honest Product Design

All models have limitations: class constraints, lighting sensitivity, background clutter, similar-looking diseases. Real-world products must handle these limitations gracefully.

**Reflection prompts**:
- What limitations did you discover in your model through testing? (e.g., confused certain diseases, sensitive to lighting, poor with cluttered backgrounds)
- How did you handle limitations in your app's design? What UI elements or text communicate these constraints?
- If you used a 6-class model instead of full dataset, how did you frame this limitation to users?
- How do real-world products (Google Photos, Shazam, translation apps) handle limitations while maintaining user trust?
- What's the difference between "MVP with clear limitations" and "incomplete product"? Where does your implementation fall?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 5. Testing ML Systems: Non-Deterministic Behavior

Traditional software testing assumes deterministic behavior: same input always produces same output. ML systems are more complex due to probabilistic nature and sensitivity to input variations.

**Reflection prompts**:
- How did testing change compared to previous weeks? What made ML testing more challenging?
- What testing strategies did you use? (e.g., known images from training set, edge cases, error injection)
- How did you validate that predictions were "reasonable" when you didn't have ground truth labels for all test images?
- What would a comprehensive test suite for an ML-powered mobile app include? (unit tests, integration tests, UI tests, model tests)
- How would you implement automated testing for ML features in CI/CD pipeline?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 6. Performance Tradeoffs: Speed, Accuracy, Size

You likely observed tradeoffs between model accuracy, inference speed, and model size during implementation or research.

**Reflection prompts**:
- What performance metrics did you measure? (inference time, preprocessing time, total request time)
- If you experimented with different models or optimizations, what tradeoffs did you notice?
- How would you make deployment decisions for a resource-constrained environment? (e.g., low-end Android device, slow network)
- What's more important for LeafGuard AI: very high accuracy with slow inference, or moderate accuracy with fast inference? Why?
- How would on-device inference (TensorFlow Lite on Android) change the tradeoffs compared to cloud inference?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 7. Real-World Application: Beyond the Classroom

Consider how the skills from Week 06 apply beyond this specific project.

**Reflection prompts**:
- What general principles from this week apply to integrating any pre-trained model (not just plant disease detection)?
- If you were building a different AI-powered app (e.g., food recognition, face detection, language translation), how would your approach change? What would stay the same?
- What skills from Week 06 are transferable to professional mobile development roles?
- If you were interviewing for a mobile developer position at an AI company, how would you describe your ML integration experience?
- What aspects of Week 06 do you feel confident about? What would you want to study further?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 8. Challenges and Solutions

Document the specific challenges you encountered this week and how you resolved them.

**Challenge 1**:
- **Problem**: _________________________________________________
- **What you tried**: _________________________________________________
- **Solution**: _________________________________________________
- **What you learned**: _________________________________________________

**Challenge 2**:
- **Problem**: _________________________________________________
- **What you tried**: _________________________________________________
- **Solution**: _________________________________________________
- **What you learned**: _________________________________________________

**Challenge 3**:
- **Problem**: _________________________________________________
- **What you tried**: _________________________________________________
- **Solution**: _________________________________________________
- **What you learned**: _________________________________________________

**Most valuable resource or insight this week**:
___________________________________________________________________
___________________________________________________________________

---

## 9. Peer Feedback and Collaboration

If you worked with classmates or received feedback on your implementation:

**Reflection prompts**:
- What feedback did you receive? How did you incorporate it?
- What approaches did classmates take that differed from yours? What did you learn from their solutions?
- If you helped a classmate debug an ML integration issue, what was the problem and how did you solve it?
- What aspects of ML integration would benefit most from collaboration versus individual work?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## 10. Looking Ahead: Next Steps

As you prepare for Week 07 (Room/SQLite database for scan history):

**Reflection prompts**:
- How will storing scan history enhance the user experience?
- What data from this week's predictions should be saved to the database? (disease name, confidence, timestamp, image path, etc.)
- What new features become possible once you have historical data? (trends, repeat detection, favorite queries)
- What questions do you have about database integration that you want to explore next week?

**Write your response here**:

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## Reflection Summary

**Overall, how confident do you feel about ML model integration after Week 06?** (1-5 scale, 5 being very confident)

Rating: ______ / 5

**What is one key insight from this week that you will remember?**

___________________________________________________________________
___________________________________________________________________

**What is one skill from this week you want to develop further?**

___________________________________________________________________
___________________________________________________________________

**How has your understanding of AI-powered mobile apps changed?**

___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

---

## Instructor Feedback

**Reflection Quality**: ______ / 10

**Comments**:
___________________________________________________________________
___________________________________________________________________
___________________________________________________________________

**Strengths observed**:
___________________________________________________________________

**Areas for growth**:
___________________________________________________________________
