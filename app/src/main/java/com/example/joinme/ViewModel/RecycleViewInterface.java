package com.example.joinme.ViewModel;

public interface RecycleViewInterface {
    void onDetailsClick(int position);
    void onJoinClick(int position);
    void onDeleteClick(int position);
    void onReportClick(int position);
    void onHappenedClick(int position, Boolean flag);
}
