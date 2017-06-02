package in.ac.iiitkota.cairn.csr.android.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.iiitkota.cairn.csr.android.R;
import in.ac.iiitkota.cairn.csr.android.adapter.MessageThreadAdapter;
import in.ac.iiitkota.cairn.csr.android.adapter.TeamProfileAdapter;
import in.ac.iiitkota.cairn.csr.android.model.Team;
import in.ac.iiitkota.cairn.csr.android.model.TeamMember;
import in.ac.iiitkota.cairn.csr.android.model.User;
import in.ac.iiitkota.cairn.csr.android.ui.fragments.MessagesThreadFragment;

import java.util.ArrayList;
import java.util.List;

public class TeamProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TeamProfileAdapter adapter;
    private String teamDescription;
    private ImageView teamImage;
    Team team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_profile);
        int position = getIntent().getIntExtra("position", -1);
        if (position == -1) finish();
        team = MessagesThreadFragment.teams.get(position);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        TextView team_name = (TextView) findViewById(R.id.team_name);
        team_name.setText(team.getName());

        teamImage = (ImageView) findViewById(R.id.team_image);
        teamImage.setBackgroundResource(MessageThreadAdapter.pickBackground(position));

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_team_profile);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        teamDescription = team.getDescription();

        adapter = new TeamProfileAdapter(this, prepareMemberList(), teamDescription);
        recyclerView.setAdapter(adapter);

    }

    private List<TeamMember> prepareMemberList() {

        List<TeamMember> listMembers = new ArrayList<>();
        for (User admin : team.getAdmins()) {
            TeamMember teamMember = new TeamMember();
            teamMember.setUser(admin);
            teamMember.setMemberType("Admin");

            listMembers.add(teamMember);
        }

        for (User member : team.getMembers()) {
            TeamMember teamMember = new TeamMember();
            teamMember.setUser(member);
            teamMember.setMemberType("");


            if (!listMembers.contains(teamMember)) listMembers.add(teamMember);
        }

        return listMembers;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
