package com.gtocore.data.recipe.builder.research;

final class ResearchRecipeDuration {

    private ResearchRecipeDuration() {}

    static int fromTotalCWU(int totalCWU, int cwut) {
        if (cwut <= 0) throw new IllegalStateException("CWU/t must be greater than zero");
        if (totalCWU <= 0) throw new IllegalStateException("Total CWU must be greater than zero");
        return Math.max(1, Math.ceilDiv(totalCWU, cwut));
    }
}
