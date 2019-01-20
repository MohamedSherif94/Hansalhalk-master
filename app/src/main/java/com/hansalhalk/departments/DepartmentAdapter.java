package com.hansalhalk.departments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;


import com.hansalhalk.R;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {

    private List<Department> departmentsList;
    ArrayList<Integer> checkedList = new ArrayList<>();

    public DepartmentAdapter(List<Department> departmentsList) {
        this.departmentsList = departmentsList;
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.department_single_layout, parent, false);

        DepartmentViewHolder vh = new DepartmentViewHolder(itemView);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull final DepartmentViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Department department = departmentsList.get(position);
        holder.mCheckBox.setText(department.getTitle());
        holder.mCheckBox.setId(department.getId());

        //in some cases, it will prevent unwanted situations
        holder.mCheckBox.setOnCheckedChangeListener(null);

        holder.mCheckBox.setChecked(department.getSelected());

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                department.setSelected(isChecked);

                int id = holder.mCheckBox.getId();

                if (checkedList.contains(id)){
                    int index = checkedList.indexOf(id);
                    checkedList.remove(index);
                }else {
                    checkedList.add(id);
                }
               // Log.v(context.getClass().getSimpleName(), checkedList.toString());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return departmentsList.size();
    }

    public ArrayList<Integer> getCheckedList() {
        return checkedList;
    }

    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public AppCompatCheckBox mCheckBox;

        public DepartmentViewHolder(View view) {
            super(view);
            mCheckBox = view.findViewById(R.id.department_single_layout_check_box);
        }

    }
}
