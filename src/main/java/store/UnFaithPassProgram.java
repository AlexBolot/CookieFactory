package store;

import recipe.Recipe;

import java.util.Map;

public class UnFaithPassProgram {
    private Map<Recipe, Reward> rewards;
    private double rewardValueToCashRatio;

    public UnFaithPassProgram(Map<Recipe, Reward> rewards) {
        this(rewards, 1);
    }

    public UnFaithPassProgram(Map<Recipe, Reward> rewards, double rewardValueToCashRatio) {
        this.rewards = rewards;
        this.rewardValueToCashRatio = rewardValueToCashRatio;
    }

    public Reward getRewardFromRecipe(Recipe recipe) {
        return this.rewards.getOrDefault(recipe, null);
    }

    public void changeReward(Recipe recipe, Reward reward) {
        this.rewards.put(recipe, reward);
    }

    /**
     * @param ratio Converting rates to apply to this. Must be positive or zero
     */
    void setRewardValueToCashRatio(double ratio) {
        if (ratio < 0) throw new IllegalArgumentException("Ratio must be positive or zero. Given ratio is : " + ratio);
        this.rewardValueToCashRatio = ratio;
    }

    /**
     * Gives the cash amount obtained from converting reward value into cash
     *
     * @param rewardValue Amount of reward value to convert. Must be positive or zero
     * @return Amount of cash obtained from convertion
     */
    public double getCashFromRewardValue(double rewardValue) {
        if (rewardValue < 0)
            throw new IllegalArgumentException("Reward Value must be positive or zero. Given value is : " + rewardValue);
        return rewardValue * this.rewardValueToCashRatio;
    }

}
