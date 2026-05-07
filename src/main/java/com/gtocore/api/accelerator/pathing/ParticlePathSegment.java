package com.gtocore.api.accelerator.pathing;

import net.minecraft.world.phys.Vec3;

/**
 * 粒子路径类，表示粒子在加速器中从一个机器的起点到终点的路径段
 * 粒子路径由多个路径段组成，每个路径段对应一个机器组件，且所有的路径段全部由直线与圆弧拼接而成
 * 每个路径段包含该段的几何路径组成单元列表，如线段，圆弧段等，以及该段的粒子束流参数，如能量损失，聚焦强度等
 * 粒子路径段还包含该段的起点和终点坐标，以及该段的长度等基本信息
 * 粒子路径段可以通过粒子路径管理器进行创建和管理
 * 粒子路径管理器负责将粒子路径段连接成完整的粒子路径，并计算粒子在路径上的运动轨迹和参数变化
 */
public record ParticlePathSegment(Vec3 start, Vec3 end, PathSegmentComponent... components) {

    public sealed interface PathSegmentComponent permits LineSegment, ArcSegment, CircleSegment {

        double length();
    }

    public record LineSegment(Vec3 start, Vec3 end) implements PathSegmentComponent {

        @Override
        public double length() {
            return start.distanceTo(end);
        }
    }

    public record ArcSegment(Vec3 center, Vec3 start, Vec3 end) implements PathSegmentComponent {

        public double radius() {
            return center.distanceTo(start);
        }

        @Override
        public double length() {
            double radius = radius();
            double angle = Math.acos(dot(start.subtract(center), (end.subtract(center))) / (radius * radius));
            return radius() * angle;
        }

        private static double dot(Vec3 a, Vec3 b) {
            return a.x() * b.x() + a.y() * b.y() + a.z() * b.z();
        }
    }

    public record CircleSegment(Vec3 center, Vec3 startEnd, double radius) implements PathSegmentComponent {

        @Override
        public double length() {
            return 2 * Math.PI * radius;
        }
    }
}
