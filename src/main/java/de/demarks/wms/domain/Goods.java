package de.demarks.wms.domain;

/**
 * 货物信息
 *
 * @author Shouran
 *
 */
public class Goods {

	private Integer id;// 货物ID 自增
	private String name;// 货物名 A3101
	private String type;// 货物类型 耳机/音响
	private String size;// 货物规格 10*5*5
	private float weight;// 货物重量kg

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "Goods{" +
				"id=" + id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", size='" + size + '\'' +
				", weight=" + weight +
				'}';
	}
}
