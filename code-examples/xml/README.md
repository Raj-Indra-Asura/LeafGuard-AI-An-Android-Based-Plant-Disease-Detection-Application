# XML Disease Library Examples

## 1. Disease XML structure

```xml
<diseases>
    <disease id="tomato_late_blight">
        <label>Tomato___Late_blight</label>
        <displayName>Tomato Late Blight</displayName>
        <symptoms>Dark water-soaked spots on leaves and stems.</symptoms>
        <treatment>Remove infected leaves and apply recommended fungicide.</treatment>
        <prevention>Avoid overhead watering and improve air circulation.</prevention>
    </disease>
</diseases>
```

## 2. Java model

```java
public class DiseaseInfo {
    public String label;
    public String displayName;
    public String symptoms;
    public String treatment;
    public String prevention;
}
```

## 3. Mapping rule

The ML model returns a label such as:

```text
Tomato___Late_blight
```

The XML library must contain the exact same label. Then the app can map prediction → explanation → treatment.

## Validation

- Unknown label should show a friendly fallback.
- Empty XML should not crash the app.
- Every model label should have one XML disease entry.
