import rclpy
from rclpy.node import Node
from rclpy.action import ActionClient
from nav2_msgs.action import NavigateToPose

class RobotNavigator(Node):
    def __init__(self):
        super().__init__('navigator')
        self.client = ActionClient(self, NavigateToPose, 'navigate_to_pose')
        self.get_logger().info('RobotNavigator 노드가 시작되었습니다.')

    def goto_waypoint(self, waypoint):
        goal_msg = NavigateToPose.Goal()
        goal_msg.pose.header.frame_id = 'map'
        goal_msg.pose.pose.position.x = waypoint['x']
        goal_msg.pose.pose.position.y = waypoint['y']
        goal_msg.pose.pose.orientation.z = waypoint['orientation_z']
        goal_msg.pose.pose.orientation.w = waypoint['orientation_w']

        self.get_logger().info(f"웨이포인트로 이동: {waypoint}")
        self.client.wait_for_server()

        # 비동기적으로 목표를 보냄
        self.send_goal_future = self.client.send_goal_async(goal_msg)
        self.send_goal_future.add_done_callback(self.goal_response_callback)

    def goal_response_callback(self, future):
        goal_handle = future.result()
        if not goal_handle.accepted:
            self.get_logger().error('목표가 거부되었습니다.')
            return

        self.get_logger().info('목표가 수락되었습니다. 결과 대기 중...')
        self.get_result_future = goal_handle.get_result_async()
        self.get_result_future.add_done_callback(self.get_result_callback)

    def get_result_callback(self, future):
        result = future.result().result
        if result:
            self.get_logger().info('목표 위치 도착 완료.')
            # 모든 목표를 완료한 후에 다음 웨이포인트로 이동
            self.next_waypoint()

    def navigate(self, waypoints):
        self.waypoints = waypoints
        self.current_waypoint_index = 0
        self.goto_waypoint(self.waypoints[self.current_waypoint_index])

    def next_waypoint(self):
        self.current_waypoint_index += 1
        if self.current_waypoint_index < len(self.waypoints):
            self.goto_waypoint(self.waypoints[self.current_waypoint_index])
        else:
            self.get_logger().info('모든 웨이포인트를 완료했습니다.')

def main(args=None):
    rclpy.init(args=args)

    navigator = RobotNavigator()

    waypoints = [
        {'x': 1.0, 'y': 2.0, 'orientation_z': 0.0, 'orientation_w': 1.0},  # 목적지1
        {'x': -3.0, 'y': 2.0, 'orientation_z': 0.0, 'orientation_w': 1.0},  # 목적지2
        {'x': 1.0, 'y': 2.0, 'orientation_z': 0.0, 'orientation_w': 1.0}   # 다시 목적지1
    ]

    navigator.navigate(waypoints)

    rclpy.spin(navigator)

    navigator.destroy_node()
    rclpy.shutdown()

if __name__ == '__main__':
    main()
