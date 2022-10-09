package com.tulun.nettytest;

/**
 * @author QiangQin
 * * @date 2021/8/1
 */
public class Person {
    private  String sex;
    private String name;
    private  int id;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    //另一种赋值方法
    public Person Sex(String sex) {
        this.sex = sex;
        return this;// 返回对象
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person Name(String name) {
        this.name = name;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person Id(int id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "Person{" +
                "sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                '}';
    }

    public static void main(String[] args) {
        Person person = new Person();
        person.setName("zhangsan");
        person.setId(1);
        person.setSex("nan");
        System.out.println(person);

        //另一种对象赋值方式
        Person person1 = new Person();
        person
                .Id(2)
                .Name("lisi")
                .Sex("nan");
        System.out.println(person1);
    }
}
