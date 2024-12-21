package org.example;




public class FitnessClubFeeCalculator {

    public interface GitHubService {
        int getLines(String gitHubRepo);
        int getWMC(String gitHubRepo);
    }

    private final GitHubService gitHubService;

    public FitnessClubFeeCalculator(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    public int calculateFee(int grade, int absenceCount, int typingMinutes, int wpm, String gitHubRepo) {
        if (grade < 2) {
            throw new IllegalArgumentException("Grade must be at least 2 to join the club.");
        }

        int baseFee = 500;
        int maxDiscount = 0;

        // Absence discount
        if (absenceCount < 5) {
            maxDiscount = Math.max(maxDiscount, 50);
        }

        // Typing speed discount
        if (wpm > 100) {
            maxDiscount = Math.max(maxDiscount, 150);
        } else if (wpm > 80) {
            maxDiscount = Math.max(maxDiscount, 100);
        } else if (wpm > 60) {
            maxDiscount = Math.max(maxDiscount, 50);
        }

        // Typing duration discount
        if ((grade == 2 && typingMinutes >= 10) ||
                (grade == 3 && typingMinutes >= 15) ||
                (grade == 4 && typingMinutes >= 20)) {
            maxDiscount = Math.max(maxDiscount, 200);
        } else if ((grade == 2 && typingMinutes >= 5) ||
                (grade == 3 && typingMinutes >= 10) ||
                (grade == 4 && typingMinutes >= 15)) {
            maxDiscount = Math.max(maxDiscount, 100);
        }


        // GitHub addiction discount
        int lines = gitHubService.getLines(gitHubRepo);
        int wmc = gitHubService.getWMC(gitHubRepo);

        if (grade == 2) {
            int githubDiscount = Math.min((lines / 1000) * 50, 200);
            maxDiscount = Math.max(maxDiscount, githubDiscount);
        } else if (wmc > 50) {
            int githubDiscount = Math.min((lines / 1000) * 50, 200);
            maxDiscount = Math.max(maxDiscount, githubDiscount);
        }

        // Apply the best discount
        return Math.max(baseFee - maxDiscount, 0); // Ensure fee is not negative
    }
}