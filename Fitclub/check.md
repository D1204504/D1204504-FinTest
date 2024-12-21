# 自我檢驗報告

## 已完成項目
### 1. **以 Java 完成以上程式**
- 已使用 Java 完成 `FitnessClubFeeCalculator` 類別，包含：
   - `calculateFee` 方法正確計算社費。
   - 依據不同條件（年級、曠課次數、打字速度、持續時間、GitHub 行數與複雜度）計算折扣。
- 已正確設計 `GitHubService` 介面，用於從 GitHub 獲取 `lines` 和 `wmc`。
  ```java
  public interface GitHubService {
      int getLines(String gitHubRepo);
      int getWMC(String gitHubRepo);
  }
### 2. **以 JUnit 進行測試，透過 Mockito 模擬 GitHubService**
- 已完成 JUnit 測試，測試類為 FitnessClubFeeCalculatorTest。
- 使用 Mockito 模擬 GitHubService，並針對以下場景進行測試：
   - 不同年級條件（例如 grade < 2 的異常檢查）。
   - 曠課次數的折扣條件。
   - 打字速度的各分段折扣。
   - 持續時間折扣。
   -  GitHub 折扣，包含大二學生和大二以上的特殊條件。

```java
FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
when(mockService.getLines("exampleRepo")).thenReturn(5000);
when(mockService.getWMC("exampleRepo")).thenReturn(60);

```   

### 3. **透過 Mockito 的 verify() 檢驗程式的互動**
-在測試中使用了 Mockito 的 verify() 方法，確認程式正確與模擬服務交互。

```java

verify(mockService).getLines("exampleRepo");
verify(mockService).getWMC("exampleRepo");
```

### 4. **透過 JUnit 進行白箱測試，Branch coverage 達到 100%**
[查看 Jacoco 覆蓋
查看 Jacoco 覆蓋率報告][htmlReport](./htmlReport/index.html)

### 5. **產生 Jacoco 涵蓋度報告**
[查看 Jacoco 覆蓋
查看 Jacoco 覆蓋率報告][htmlReport](./htmlReport/index.html)