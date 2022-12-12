/**
 * 
 * @author 최진실
 *
 */
package com.rence.dashboard.dao;

import java.util.List;

import com.rence.backoffice.model.BackOfficeDTO;
import com.rence.backoffice.model.BackOfficeOperatingTimeDTO;
import com.rence.dashboard.model.BOPaymentVO;
import com.rence.dashboard.model.CommentInsertVO;
import com.rence.dashboard.model.CommentListQView;
import com.rence.dashboard.model.CommentSummaryViewDTO;
import com.rence.dashboard.model.CommentVO;
import com.rence.dashboard.model.ReservationView;
import com.rence.dashboard.model.ReserveListView;
import com.rence.dashboard.model.ReserveSummaryViewDTO;
import com.rence.dashboard.model.ReserveUpdateVO;
import com.rence.dashboard.model.ReviewListView;
import com.rence.dashboard.model.RoomDTO;
import com.rence.dashboard.model.RoomSummaryViewDTO;
import com.rence.dashboard.model.SalesSettlementDetailView;
import com.rence.dashboard.model.SalesSettlementSummaryViewDTO;
import com.rence.dashboard.model.SalesSettlementViewVO;
import com.rence.dashboard.model.ScheduleEntity;
import com.rence.dashboard.model.ScheduleListView;

public interface DashboardDAO {

	public List<ReserveSummaryViewDTO> reserve_summary_selectAll(String backoffice_no);

	public List<CommentSummaryViewDTO> comment_summary_selectAll(String backoffice_no);

	public SalesSettlementSummaryViewDTO payment_summary_selectOne(String backoffice_no);

	public RoomSummaryViewDTO room_summary_selectOne(String backoffice_no);

	public long dashboard_room_list_cnt(String backoffice_no);

	public List<RoomDTO> dashboard_room_list(String backoffice_no, Integer page);

	public BackOfficeDTO select_one_backoffice_info(String backoffice_no);

	public int backoffice_insertOK_room(String backoffice_no, RoomDTO rvo);

	public RoomDTO select_one_room_info(String backoffice_no, String room_no);

	public int backoffice_updateOK_room(String backoffice_no, RoomDTO rvo);

	public void backoffice_deleteOK_room(String backoffice_no, String room_no);

	public long backoffice_qna_selectAll_cnt(String backoffice_no);

	public List<CommentListQView> backoffice_qna_selectAll(String backoffice_no, Integer page);

	public CommentVO backoffice_insert_comment(String backoffice_no, String room_no, String comment_no);

	public int backoffice_insertOK_comment(CommentInsertVO cvo);

	public int update_comment_state_T(String backoffice_no, String comment_no);

	public int backoffice_deleteOK_comment(String backoffice_no, String comment_no);

	public int update_comment_state_F(String backoffice_no, String mother_no);

	public long backoffice_review_selectAll_cnt(String backoffice_no);

	public List<ReviewListView> backoffice_review_selectAll(String backoffice_no, Integer page);

	public int backoffice_reserve_selectAll_cnt(String backoffice_no, String reserve_state);

	public List<ReserveListView> backoffice_reserve_selectAll(String backoffice_no, String reserve_state, int start_row, int end_row);

	public int backoffice_search_reserve_cnt(String backoffice_no, String searchword, String reserve_state);

	public List<ReserveListView> backoffice_search_reserve(String backoffice_no, String searchword,
			String reserve_state, int start_row, int end_row);

	public SalesSettlementDetailView backoffice_sales_selectOne(String backoffice_no, String sales_date);

	public long backoffice_sales_selectAll_cnt(String backoffice_no);

	public List<SalesSettlementViewVO> backoffice_sales_selectAll(String backoffice_no, Integer page);

	public int backoffice_updateOK_sales(String backoffice_no, String room_no, String payment_no);

	public BackOfficeDTO backoffice_setting_selectOne(BackOfficeDTO bvo);

	public BackOfficeDTO backoffice_select_pw(BackOfficeDTO bvo);

	public int backoffice_setting_delete(BackOfficeDTO bvo);

	public int backoffice_room_deleteALL(BackOfficeDTO bvo);

	public BackOfficeOperatingTimeDTO backoffice_setting_selectOne_operatingtime(String backoffice_no);

	public int backoffice_updateOK_host(BackOfficeDTO bvo);

	public int backoffice_updateOK_opt(BackOfficeOperatingTimeDTO ovo);

	public List<ScheduleListView> backoffice_schedule_list(String backoffice_no, String not_sdate, String not_edate,
			String not_stime, String not_etime, String off_type);

	public int backoffice_schedueOK(String backoffice_no, String not_s, String not_e, String room_no);

	public int backoffice_reservation_cnt(String backoffice_no, String not_sdate, String not_edate, String not_stime,
			String not_etime, String room_no, String off_type);

	public List<ReservationView> backoffice_reservation(String backoffice_no, String not_sdate, String not_edate,
			String not_stime, String not_etime, String room_no, String off_type, int min, int max);

	public BOPaymentVO backoffice_reservation_cancel(String backoffice_no, String reserve_no, String user_no);

	public BackOfficeDTO backoffice_select_companyname(String backoffice_no);

	public List<ScheduleEntity> backoffice_schedule_calendar(String backoffice_no);

	public RoomDTO backoffice_schedule_calendar_room_name(String room_no);

	public int backoffice_schedule_cancel(String backoffice_no, String schedule_no);

	public void reserve_state_auto_update();

	public ReserveUpdateVO select_one_false_reserve(String reserve_stime, String reserve_etime, String room_no);

	public void reserve_auto_delete(String reserve_no);


}
