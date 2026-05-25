# Sample Plant Disease Images

This folder contains sample test images for testing the LeafGuard AI application. These images are used to validate both cloud (FastAPI) and offline (TensorFlow Lite) prediction modes.

## 📁 Folder Structure

```
sample-images/
├── README.md (this file)
├── healthy/
│   ├── tomato_healthy_01.jpg
│   ├── tomato_healthy_02.jpg
│   ├── potato_healthy_01.jpg
│   └── ...
├── early_blight/
│   ├── tomato_early_blight_01.jpg
│   ├── tomato_early_blight_02.jpg
│   ├── potato_early_blight_01.jpg
│   └── ...
├── late_blight/
│   ├── tomato_late_blight_01.jpg
│   ├── potato_late_blight_02.jpg
│   └── ...
└── other_diseases/
    ├── tomato_bacterial_spot_01.jpg
    ├── tomato_leaf_mold_01.jpg
    └── ...
```

## 🎯 Purpose

### Testing Cloud Mode
- Upload test images through Android app
- Verify FastAPI backend processes images correctly
- Validate prediction accuracy for known diseases
- Test edge cases (blurry images, wrong crops, etc.)

### Testing Offline Mode
- Test TensorFlow Lite inference with same images
- Compare cloud vs offline predictions
- Measure inference latency differences
- Verify offline mode works without internet

### Evidence Collection
- Screenshot prediction results for each test image
- Document accuracy in test case table
- Save results to `docs/evidence/week-06/` and `week-09/`

## 📸 Image Requirements

### Minimum Image Set

You MUST collect at least **30 test images** covering:

1. **Tomato Healthy** (3+ images)
   - Clear tomato leaf with no disease
   - Good lighting, in-focus
   - File naming: `tomato_healthy_01.jpg`, `tomato_healthy_02.jpg`, etc.

2. **Tomato Early Blight** (3+ images)
   - Brown spots with concentric rings
   - Various stages of disease severity
   - File naming: `tomato_early_blight_01.jpg`, etc.

3. **Tomato Late Blight** (3+ images)
   - Dark lesions, water-soaked appearance
   - Clear disease symptoms visible
   - File naming: `tomato_late_blight_01.jpg`, etc.

4. **Potato Healthy** (3+ images)
   - Healthy potato plant leaves
   - File naming: `potato_healthy_01.jpg`, etc.

5. **Potato Early Blight** (3+ images)
   - Similar to tomato but on potato leaves
   - File naming: `potato_early_blight_01.jpg`, etc.

6. **Potato Late Blight** (3+ images)
   - Late blight on potato leaves
   - File naming: `potato_late_blight_01.jpg`, etc.

7. **Other Diseases** (12+ images covering other classes your model supports)
   - Examples: Tomato Bacterial Spot, Tomato Leaf Mold, Tomato Septoria Leaf Spot, Tomato Spider Mites, Tomato Yellow Leaf Curl Virus, etc.
   - Depends on your model's supported classes

### Image Quality Guidelines

**Resolution**
- Minimum: 500x500 pixels
- Maximum: 3000x3000 pixels (will be resized by app)
- Recommended: 1024x1024 pixels

**Format**
- Accepted: JPEG (.jpg, .jpeg), PNG (.png)
- Preferred: JPEG for smaller file size
- Avoid: BMP, GIF, TIFF (won't work on Android easily)

**Content**
- **Single leaf focus**: Image should show primarily one leaf
- **Clear disease symptoms**: If diseased, symptoms should be visible
- **Good lighting**: Natural daylight or bright indoor lighting
- **In focus**: Avoid blurry images
- **Minimal background**: Background should not distract from leaf
- **No watermarks**: Remove any text overlays or watermarks

**File Size**
- Minimum: 50 KB
- Maximum: 10 MB
- Typical: 200-500 KB

**File Naming Convention**
```
{crop}_{disease}_{number}.jpg

Examples:
tomato_healthy_01.jpg
tomato_early_blight_01.jpg
potato_late_blight_03.jpg
pepper_bacterial_spot_02.jpg
```

## 🔍 Where to Get Sample Images

### Option 1: PlantVillage Dataset (Recommended)
- **Source**: https://github.com/spMohanty/PlantVillage-Dataset
- **License**: Open access (public domain)
- **Content**: 54,000+ images of 14 crop species, 38 disease classes
- **Download**: Clone repository or download specific classes
- **Usage**: Select 30-50 representative images from classes your model supports
- **Note**: This is the standard dataset used for plant disease detection research

### Option 2: Kaggle Plant Disease Datasets
- **Source**: https://www.kaggle.com/datasets (search "plant disease")
- **Popular datasets**:
  - PlantVillage (same as above)
  - Plant Pathology 2020 FGVC7
  - Plant Disease Expert
- **License**: Check each dataset's license (most are CC0 or MIT)
- **Download**: Requires Kaggle account (free)

### Option 3: Google Images (Use with Caution)
- **Search terms**: "[crop] [disease name] leaf", example: "tomato early blight leaf"
- **Filters**: Tools → Usage Rights → Creative Commons licenses or Labeled for reuse
- **Warning**: Verify image quality and relevance
- **Note**: Check license for each image; avoid copyrighted images

### Option 4: Take Your Own Photos (Best for Originality)
- **Method**: Visit local farms, gardens, or plant nurseries
- **Permission**: Always ask permission before photographing
- **Equipment**: Smartphone camera is sufficient (8MP+ recommended)
- **Advantages**: Original images, can control quality, demonstrate field work
- **Challenges**: May not find diseased plants easily in some regions

### Option 5: Research Paper Supplementary Materials
- **Source**: Papers on plant disease detection often include sample images
- **License**: Usually available for research purposes; cite the source
- **Quality**: High quality, expert-validated disease labels

## 📝 Documentation Requirements

### Image Source Documentation

Create `sample-images/IMAGE_SOURCES.txt` documenting:

```
Image Source Documentation for LeafGuard AI

Dataset: PlantVillage
Source URL: https://github.com/spMohanty/PlantVillage-Dataset
License: CC0 1.0 Universal (CC0 1.0) Public Domain Dedication
Date Downloaded: 2024-XX-XX
Classes Used:
  - Tomato Healthy (3 images)
  - Tomato Early Blight (3 images)
  - Tomato Late Blight (3 images)
  - Potato Healthy (3 images)
  - Potato Early Blight (3 images)
  - Potato Late Blight (3 images)
  - ... (list all)

Citation:
Hughes, D. P., & Salathé, M. (2015). An open access repository of images on plant health to enable the development of mobile disease diagnostics. arXiv preprint arXiv:1511.08060.

Additional Images:
  - Own photographs: 5 images from [location], taken on [date]
  - Kaggle [dataset name]: 10 images, license [license type]

All images used are either:
1. Public domain
2. Creative Commons licensed
3. Original photographs

No copyrighted images were used without permission.
```

### Test Results Documentation

Create `sample-images/TEST_RESULTS.md`:

```markdown
# Test Results for Sample Images

## Cloud Mode Test Results

| Image | True Label | Predicted Label | Confidence | Correct? | Notes |
|-------|-----------|-----------------|------------|----------|-------|
| tomato_healthy_01.jpg | Healthy | Healthy | 98.5% | ✅ Yes | Good prediction |
| tomato_early_blight_01.jpg | Early Blight | Early Blight | 92.3% | ✅ Yes | Clear symptoms |
| ... | ... | ... | ... | ... | ... |

**Accuracy**: 28/30 = 93.3%

## Offline Mode Test Results (TensorFlow Lite)

| Image | True Label | Predicted Label | Confidence | Correct? | Latency (ms) |
|-------|-----------|-----------------|------------|----------|--------------|
| tomato_healthy_01.jpg | Healthy | Healthy | 97.8% | ✅ Yes | 145 ms |
| ... | ... | ... | ... | ... | ... |

**Accuracy**: 26/30 = 86.7%
**Average Latency**: 158 ms

## Cloud vs Offline Comparison

| Metric | Cloud Mode | Offline Mode |
|--------|-----------|-------------|
| Accuracy | 93.3% | 86.7% |
| Avg Confidence | 91.2% | 88.5% |
| Avg Latency | 2.3 seconds | 158 ms |
| Internet Required | Yes | No |

## Failed Predictions Analysis

**Cloud Mode Failures** (2 images):
1. `potato_early_blight_03.jpg` - Predicted as Healthy (87%), actually Early Blight
   - Reason: Very early stage, symptoms not clear
2. `tomato_late_blight_05.jpg` - Predicted as Early Blight (76%), actually Late Blight
   - Reason: Similar symptom appearance

**Offline Mode Additional Failures** (4 images beyond cloud failures):
1-4. [List additional failures]

## Conclusions

- Cloud model performs better (93.3% vs 86.7%)
- Offline model is much faster (158ms vs 2.3s)
- Both models struggle with early-stage diseases
- Trade-off between accuracy and speed is acceptable for mobile use
```

## 🧪 Testing Workflow

### Week 6: Cloud Mode Testing

1. **Setup**
   - Start FastAPI backend on laptop
   - Connect phone to same Wi-Fi
   - Configure Android app with backend URL

2. **Testing**
   - For each test image:
     - Load image to phone (via USB or Google Drive)
     - Open LeafGuard AI app
     - Select image from gallery
     - Submit for prediction
     - Record result, confidence, and latency
     - Take screenshot
     - Save to evidence folder

3. **Documentation**
   - Fill in TEST_RESULTS.md cloud mode table
   - Calculate accuracy percentage
   - Analyze failure cases
   - Save screenshots to `docs/evidence/week-06/cloud-predictions/`

### Week 9: Offline Mode Testing

1. **Setup**
   - Ensure TFLite model and labels.txt in app assets
   - Build and install app with TFLite
   - Enable airplane mode (to confirm offline)

2. **Testing**
   - For each test image:
     - Select image from gallery
     - Choose offline mode in app
     - Submit for prediction
     - Record result, confidence, and latency
     - Take screenshot
     - Compare with cloud mode result

3. **Documentation**
   - Fill in TEST_RESULTS.md offline mode table
   - Calculate accuracy percentage
   - Calculate average latency
   - Create comparison table
   - Save screenshots to `docs/evidence/week-09/offline-predictions/`

### Week 11: Edge Case Testing

Test with unusual images:
- [ ] Very blurry leaf image
- [ ] Image of non-leaf object (e.g., hand, wall)
- [ ] Image of wrong crop (e.g., apple if model expects tomato/potato)
- [ ] Very dark (underexposed) image
- [ ] Very bright (overexposed) image
- [ ] Multiple leaves in one image
- [ ] Leaf with water droplets
- [ ] Extreme close-up
- [ ] Very small resolution (200x200)
- [ ] Very large resolution (4000x4000)

Document how app handles these edge cases:
- Does it crash?
- Does it show error message?
- Does it give low confidence predictions?
- Is the error handling user-friendly?

## 📋 Student Checklist

Before Week 6:
- [ ] Collected at least 30 test images
- [ ] Organized images by disease class
- [ ] Renamed images following naming convention
- [ ] Created IMAGE_SOURCES.txt with proper citations
- [ ] Verified image quality (resolution, focus, lighting)
- [ ] Transferred images to phone for testing

During Week 6 (Cloud Mode):
- [ ] Tested all images with cloud mode
- [ ] Documented results in TEST_RESULTS.md
- [ ] Took screenshots of predictions
- [ ] Saved evidence to docs/evidence/week-06/

During Week 9 (Offline Mode):
- [ ] Tested all images with offline mode
- [ ] Documented results in TEST_RESULTS.md
- [ ] Compared cloud vs offline results
- [ ] Saved evidence to docs/evidence/week-09/

During Week 11 (Edge Cases):
- [ ] Tested edge cases
- [ ] Documented error handling
- [ ] Updated TEST_RESULTS.md with edge case section

Before Final Submission:
- [ ] All test results documented
- [ ] All screenshots saved
- [ ] TEST_RESULTS.md complete
- [ ] IMAGE_SOURCES.txt complete
- [ ] Images committed to GitHub (or documented why not if too large)

## 💾 GitHub Considerations

### Committing Images to Repository

**Option 1: Commit Small Set (Recommended)**
- Select 10-15 representative images (< 5MB total)
- Commit to repository for easy access
- Document in README that full set available on request

**Option 2: Use Git LFS for Larger Sets**
- If you have 30+ images (> 10MB total)
- Use Git Large File Storage (Git LFS)
- Configure .gitattributes: `*.jpg filter=lfs diff=lfs merge=lfs -text`
- Push images via LFS

**Option 3: External Hosting**
- Upload full image set to Google Drive / Dropbox
- Include link in README.md
- Make sure link allows public access (anyone with link)
- Backup to USB drive for physical submission

**Option 4: GitHub Release Assets**
- Create GitHub release (e.g., v1.0)
- Attach images as release assets (ZIP file)
- Reference release in README

## 🔗 References

### Academic Sources
1. Hughes, D. P., & Salathé, M. (2015). An open access repository of images on plant health to enable the development of mobile disease diagnostics. *arXiv preprint arXiv:1511.08060*.

2. Mohanty, S. P., Hughes, D. P., & Salathé, M. (2016). Using deep learning for image-based plant disease detection. *Frontiers in plant science*, 7, 1419.

### Datasets
- PlantVillage Dataset: https://github.com/spMohanty/PlantVillage-Dataset
- Plant Pathology 2020: https://www.kaggle.com/c/plant-pathology-2020-fgvc7
- Kaggle Plant Disease: https://www.kaggle.com/datasets/search?q=plant+disease

### Image Sources Guidelines
- Creative Commons Search: https://search.creativecommons.org/
- Understanding CC Licenses: https://creativecommons.org/licenses/

## ❓ FAQ

**Q: Can I use images from Google Images?**
A: Only if they have a Creative Commons license or are labeled for reuse. Use Google Images → Tools → Usage Rights → Creative Commons licenses. Always cite the source.

**Q: How many images do I need?**
A: Minimum 30 for testing (3 per major class). More is better for comprehensive testing.

**Q: What if my model supports 20 classes but I only have images for 6?**
A: That's acceptable for an academic project. Document which classes you tested. Focus on Tomato and Potato diseases (healthy, early blight, late blight) as these are common.

**Q: Can I use images from research papers?**
A: Yes, if the paper provides images as supplementary material for research purposes. Cite the paper properly.

**Q: What if I can't find diseased leaf images?**
A: Use PlantVillage dataset - it has over 50,000 images covering 38 disease classes. It's specifically designed for this purpose.

**Q: Should I augment the images (rotate, flip, etc.)?**
A: No need for testing. You're testing the model, not training it. Use original images.

**Q: What if my predictions are all wrong (< 50% accuracy)?**
A: Check: (1) Are you using the correct labels.txt? (2) Is preprocessing correct (resize, normalization)? (3) Are test images matching classes the model was trained on? Document this issue and focus on mobile engineering aspects.

**Q: Can I test with images of my own plants?**
A: Yes! This shows initiative. Take photos, label them (if you know the disease), test, and document results. Include these as "Original photographs" in IMAGE_SOURCES.txt.

## 📧 Need Help?

If you encounter issues:
1. Check Week 6 and Week 9 build-task.md files
2. Review model/README.md for preprocessing requirements
3. Ask in course discussion forum or contact instructor
4. Check model documentation for supported classes

---

**Remember**: The quality of your test images directly affects how well you can evaluate your application. Invest time in collecting good, diverse test images!

Good luck testing LeafGuard AI! 🌿🔍
