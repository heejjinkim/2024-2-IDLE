package com.example.__2_IDLE.ros.data_sender.message;

import com.example.__2_IDLE.global.model.Pose;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalMessage extends PublishMessage {

    private Header header;
    private PoseMessage pose;

    public GoalMessage(Pose position) {
        this.header = new Header("map");
        this.pose = new PoseMessage(position);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {
        @JsonProperty("frame_id")
        private String frameId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PoseMessage {
        private Position position;
        private Orientation orientation;

        public PoseMessage(Pose position) {
            this.position = new Position(position.getX(), position.getY(), 0.0);
            this.orientation = new Orientation(0.0, 0.0, 0.0, 1.0);
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Position {
            private double x;
            private double y;
            private double z;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Orientation {
            private double x;
            private double y;
            private double z;
            private double w;
        }
    }
}
