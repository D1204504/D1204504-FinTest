package example;

import org.example.FitnessClubFeeCalculator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class FitnessClubFeeCalculatorTest {

    @Test
    public void testCalculateFee() {
        // Mock GitHubService
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(5000);
        when(mockService.getWMC("exampleRepo")).thenReturn(60);

        // Instantiate FitnessClubFeeCalculator with the mock
        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);

        // Test input: grade=3, absenceCount=3, typingMinutes=15, wpm=85, gitHubRepo="exampleRepo"
        int fee = calculator.calculateFee(3, 3, 15, 85, "exampleRepo");

        // Assert the expected fee
        assertEquals(300, fee); // Expected fee based on the logic

        // Verify GitHubService interactions
        verify(mockService).getLines("exampleRepo");
        verify(mockService).getWMC("exampleRepo");
    }

    @Test
    public void testGradeTooLow() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);

        // Test with grade < 2, should throw an exception
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateFee(1, 3, 15, 85, "exampleRepo"));
    }
    @Test
    public void testNoAbsenceDiscount() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(5000);
        when(mockService.getWMC("exampleRepo")).thenReturn(60);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 5, 15, 85, "exampleRepo");
        assertEquals(300, fee); // Expects other discounts but no absence discount
    }
    @Test
    public void testMaxTypingSpeedDiscount() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(5000);
        when(mockService.getWMC("exampleRepo")).thenReturn(60);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 3, 15, 101, "exampleRepo");
        assertEquals(300, fee); // wpm > 100, typing speed discount = 150
    }
    @Test
    public void testTypingMinutesDiscount200() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(5000);
        when(mockService.getWMC("exampleRepo")).thenReturn(60);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 3, 15, 85, "exampleRepo");
        assertEquals(300, fee); // Typing duration discount = 200
    }
    @Test
    public void testGitHubDiscountGrade2() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(2000);
        when(mockService.getWMC("exampleRepo")).thenReturn(40);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(2, 3, 10, 85, "exampleRepo");
        assertEquals(300, fee); // GitHub discount = 100
    }
    @Test
    public void testGitHubDiscountGrade3NoWMC() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(5000);
        when(mockService.getWMC("exampleRepo")).thenReturn(30);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 3, 15, 85, "exampleRepo");
        assertEquals(300, fee); // GitHub discount not applied due to wmc <= 50
    }
    @Test
    public void testWpmDiscount60To80() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(2000);
        when(mockService.getWMC("exampleRepo")).thenReturn(40);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 3, 15, 70, "exampleRepo");
        assertEquals(300, fee); // Typing speed discount = 50
    }
    @Test
    public void testTypingMinutesGrade3Discount() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(2000);
        when(mockService.getWMC("exampleRepo")).thenReturn(40);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(3, 3, 10, 85, "exampleRepo");
        assertEquals(400, fee); // Typing duration discount = 100
    }
    @Test
    public void testTypingMinutesGrade4Discount() {
        FitnessClubFeeCalculator.GitHubService mockService = mock(FitnessClubFeeCalculator.GitHubService.class);
        when(mockService.getLines("exampleRepo")).thenReturn(2000);
        when(mockService.getWMC("exampleRepo")).thenReturn(40);

        FitnessClubFeeCalculator calculator = new FitnessClubFeeCalculator(mockService);
        int fee = calculator.calculateFee(4, 3, 15, 85, "exampleRepo");
        assertEquals(400, fee); // Typing duration discount = 100
    }

}
