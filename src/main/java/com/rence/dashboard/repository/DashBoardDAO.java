/**
 * 
 * @author 최진실
 *
 */
package com.rence.dashboard.repository;

import java.util.List;
import java.util.Map;

import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.dashboard.model.CommentInsertVO;
import com.rence.dashboard.model.CommentListQView;
import com.rence.dashboard.model.CommentSummaryView;
import com.rence.dashboard.model.CommentVO;
import com.rence.dashboard.model.ReserveListView;
import com.rence.dashboard.model.ReserveSummaryView;
import com.rence.dashboard.model.ReviewListView;
import com.rence.dashboard.model.RoomInsertVO;
import com.rence.dashboard.model.RoomSummaryView;
import com.rence.dashboard.model.RoomVO;
import com.rence.dashboard.model.SalesSettlementDetailView;
import com.rence.dashboard.model.SalesSettlementSummaryView;
import com.rence.dashboard.model.SalesSettlementViewVO;
import com.rence.dashboard.model.ScheduleListView;

public interface DashBoardDAO {

	public List<ReserveSummaryView> reserve_summary_selectAll(String backoffice_no);

	public List<CommentSummaryView> comment_summary_selectAll(String backoffice_no);

	public SalesSettlementSummaryView payment_summary_selectOne(String backoffice_no);

	public RoomSummaryView room_summary_selectOne(String backoffice_no);

	public long dashboard_room_list_cnt(String backoffice_no);

	public List<RoomVO> dashboard_room_list(String backoffice_no, Integer page);

	public BackOfficeVO select_one_backoffice_info(String backoffice_no);

	public int backoffice_insertOK_room(String backoffice_no, RoomInsertVO rvo);

	public RoomVO select_one_room_info(String backoffice_no, String room_no);

	public int backoffice_updateOK_room(String backoffice_no, RoomInsertVO rvo);

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

	public BackOfficeVO backoffice_setting_selectOne(BackOfficeVO bvo);

	public BackOfficeVO backoffice_select_pw(BackOfficeVO bvo);

	public int backoffice_setting_delete(BackOfficeVO bvo);

	public int backoffice_room_deleteALL(BackOfficeVO bvo);

	public BackOfficeOperatingTimeVO backoffice_setting_selectOne_operatingtime(String backoffice_no);

	public int backoffice_updateOK_host(BackOfficeVO bvo);

	public int backoffice_updateOK_opt(BackOfficeOperatingTimeVO ovo);

	public List<ScheduleListView> backoffice_schedule_list(String backoffice_no, String not_sdate, String not_edate,
			String not_stime, String not_etime, String off_type);

	public int backoffice_schedueOK(String backoffice_no, String not_s, String not_e, String room_no);


}
