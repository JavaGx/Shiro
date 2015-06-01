package cn.gx.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE,region="myCache")
public class Permission implements Serializable {

	private Integer id;
	private String permission_name;
	private String url;
	@JsonIgnore
	private List<Role> roles=new ArrayList<Role>();
	
	
	public Permission() {
		super();
	}

	public Permission(Integer id, String permission_name, String url) {
		super();
		this.id = id;
		this.permission_name = permission_name;
		this.url = url;
	}

	public Permission(String permission_name, String url) {
		super();
		this.permission_name = permission_name;
		this.url = url;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(length=100,nullable=false)
	public String getPermission_name() {
		return permission_name;
	}
	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}
	@Column(length=100,nullable=false)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@ManyToMany(mappedBy="perms")
	public List<Role> getRoles() {
		return roles;
	}
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
	@Override
	public String toString() {
		return "Permission [id=" + id + ", permission_name=" + permission_name
				+ ", url=" + url + ", roles=" + roles + "]";
	}
	
	
}
