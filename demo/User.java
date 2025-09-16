// User.java - 用户实体类
public class User {
    private String name;
    private int age;

    // 构造函数
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getter 和 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 自定义方法
    public void introduce() {
        System.out.println("大家好，我叫 " + name + "，今年 " + age + " 岁。");
    }
}