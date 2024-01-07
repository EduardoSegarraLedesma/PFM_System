package Data.Budget;

import java.math.BigDecimal;
import java.util.Date;

public class FinancialGoal {
    private Integer goalId;
    private String userId;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private Date startDate;
    private Date endDate;

    public FinancialGoal(Integer goalId, String userId, String description,
                         BigDecimal targetAmount, BigDecimal currentAmount,
                         Date startDate, Date endDate) {
        this.goalId = goalId;
        this.userId = userId;
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public Integer getGoalId() {
        return goalId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

}