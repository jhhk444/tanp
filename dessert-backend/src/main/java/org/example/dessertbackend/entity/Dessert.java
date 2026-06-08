package org.example.dessertbackend.entity;
import java.time.LocalDateTime;

public class Dessert {
    private Integer id;
    private String image;       // 图片路径
    private String name;        // 甜点名称
    private Integer categoryId; // 所属分类ID
    private Double price;       // 单价
    private String description;// 甜点描述
    private LocalDateTime createTime; // 发布日期

    // Getter & Setter
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getImage() {return image;}
    public void setImage(String image) {this.image = image;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public Integer getCategoryId() {return categoryId;}
    public void setCategoryId(Integer categoryId) {this.categoryId = categoryId;}
    public Double getPrice() {return price;}
    public void setPrice(Double price) {this.price = price;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public LocalDateTime getCreateTime() {return createTime;}
    public void setCreateTime(LocalDateTime createTime) {this.createTime = createTime;}
}