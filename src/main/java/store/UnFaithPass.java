package store;

import recipe.Recipe;

import java.util.Map;

public class UnFaithPass {
    Map<Recipe, Reward> rewards;

    public UnFaithPass(Map<Recipe, Reward> rewards) {
        this.rewards = rewards;
    }

    public Reward getRewardFromRecipe(Recipe recipe) {
        return this.rewards.get(recipe);
    }

    public void changeReward (Recipe recipe, Reward reward) {
        this.rewards.put(recipe,reward);
    }
}
