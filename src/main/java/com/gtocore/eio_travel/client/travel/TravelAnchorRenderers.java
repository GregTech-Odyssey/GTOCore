package com.gtocore.eio_travel.client.travel;

import com.gtocore.eio_travel.AbstractTravelTarget;

public class TravelAnchorRenderers {

    public static TravelAnchorRenderer<AbstractTravelTarget> getRenderer() {
        return travelData -> new TravelAnchorRenderer.RenderInfo(
                travelData.getVisibility(),
                travelData.getName(),
                travelData.getIcon());
    }
}
