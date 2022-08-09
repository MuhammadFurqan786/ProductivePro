package eu.faircode.netguard;

public class UserModel {

    String uid,dob,email,name,phone;

    public UserModel(String uid, String dob, String email, String name, String phone) {
        this.uid = uid;
        this.dob = dob;
        this.email = email;
        this.name = name;
        this.phone = phone;
    }

    public UserModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
