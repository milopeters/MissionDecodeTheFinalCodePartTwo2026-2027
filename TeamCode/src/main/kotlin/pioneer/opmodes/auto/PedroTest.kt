package pioneer.opmodes.auto

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import com.pedropathing.ivy.Scheduler.*
import com.pedropathing.ivy.pedro.PedroCommands.*
import com.pedropathing.ivy.groups.Groups.*

import com.pedropathing.follower.Follower
import com.pedropathing.geometry.BezierLine
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import pioneer.Bot
import pioneer.BotType
import pioneer.PedroConstants
import pioneer.opmodes.BaseOpMode

@Autonomous(name = "Pedro Test")
class PedroTest : BaseOpMode() {
    lateinit var follower: Follower

    private val pose1 = Pose(0.0, 0.0, Math.toRadians(0.0))
    private val pose2 = Pose(20.0, 0.0, Math.toRadians(0.0))
    private val pose3 = Pose(20.0, 20.0, Math.toRadians(0.0))
    private val pose4 = Pose(0.0, 0.0, Math.toRadians(180.0))

    private val path1 = follower.pathBuilder()
        .addPath(BezierLine(pose1, pose2))
        .setLinearHeadingInterpolation(pose1.heading, pose2.heading)
        .build()
    private val path2 = follower.pathBuilder()
        .addPath(BezierLine(pose2, pose3))
        .setLinearHeadingInterpolation(pose2.heading, pose3.heading)
        .build()
    private val path3 = follower.pathBuilder()
        .addPath(BezierLine(pose3, pose4))
        .setLinearHeadingInterpolation(pose3.heading, pose4.heading)
        .build()

    override fun onInit() {
        bot = Bot.fromType(BotType.MECANUM_BOT, hardwareMap)
        Scheduler.reset()
        follower = PedroConstants.createFollower(hardwareMap)!!;
        follower.setStartingPose(pose1);
    }

    override fun init_loop() {
        schedule(sequential(
            follow(follower, path1),
            follow(follower, path2, true),
            follow(follower, path3, true),
        ));
    }

    override fun onLoop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("x", follower.pose.x);
        telemetry.addData("y", follower.pose.y);
        telemetry.addData("heading", follower.pose.heading);
        telemetry.update();
    }
}