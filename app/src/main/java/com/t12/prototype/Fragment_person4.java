package com.t12.prototype;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Fragment_person4 extends Fragment {

    public static Fragment_person4 newInstance() {
        return new Fragment_person4();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_person, container, false);

        RecyclerView recyclerView1 = rootView.findViewById(R.id.recyclerView_albumtab_1);
        RecyclerView recyclerView2 = rootView.findViewById(R.id.recyclerView_albumtab_2);

        TextView person_fragment_title_textView;
        person_fragment_title_textView = rootView.findViewById(R.id.person_fragment_title);
        person_fragment_title_textView.setText("Andrew");

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        Person_albumtab_Adapter adapter1 = new Person_albumtab_Adapter(3);
        Person_albumtab_Adapter adapter2 = new Person_albumtab_Adapter(3);

        // 테스트용 데이터
        adapter1.addItem(new Person_albumtab("최근 항목","12,563"));
        adapter1.addItem(new Person_albumtab("일상 사진","547"));
        adapter1.addItem(new Person_albumtab("기타 사진","53"));
        adapter1.addItem(new Person_albumtab("이런 사진","533"));
        adapter1.addItem(new Person_albumtab("저런 사진","677"));

        adapter2.addItem(new Person_albumtab("혼자","33"));
        adapter2.addItem(new Person_albumtab("같이","1,270"));

        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);


        return rootView;
    }
}
