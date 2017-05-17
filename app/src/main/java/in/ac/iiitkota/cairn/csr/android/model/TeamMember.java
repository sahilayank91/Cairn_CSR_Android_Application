package in.ac.iiitkota.cairn.csr.android.model;

/**
 * Created by SS Verma on 26-03-2017.
 */

public class TeamMember {

    private String memberType;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    @Override
    public boolean equals(Object obj) {
        TeamMember member=(TeamMember)obj;
        return member.getUser().equals(this.user);

    }
}
