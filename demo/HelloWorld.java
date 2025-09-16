// HelloWorld.java - 主程序入口
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("=== Java Demo 程序启动 ===");

        // 创建 User 对象
        User user1 = new User("张三", 25);
        User user2 = new User("李四", 30);

        // 调用方法
        user1.introduce();
        user2.introduce();

        // 修改属性
        user1.setAge(26);
        System.out.println(user1.getName() + " 的新年龄是：" + user1.getAge());

        System.out.println("=== 程序结束 ===");
    }
}