package cn.gx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="myCache")
public class Role implements Serializable {
	
	private Integer id;
	private String role_name;
	private String description;
	@JsonIgnore
	private List<User> users=new ArrayList<User>();
	@JsonIgnore
	private List<Permission> perms=new ArrayList<Permission>();
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(length=20,nullable=false)
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	@Column(length=100)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,mappedBy="roles")
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	@ManyToMany(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY)
	@JoinTable(name="role_permission",
		joinColumns={@JoinColumn(name="role_id",referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="permission_id",referencedColumnName="id")})
	public List<Permission> getPerms() {
		return perms;
	}
	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}
	@Override
	public String toString() {
		return "Role [id=" + id + ", role_name=" + role_name + ", description="
				+ description + ", users=" + null + ", perms=" + null + "]";
	}
	
	
}
