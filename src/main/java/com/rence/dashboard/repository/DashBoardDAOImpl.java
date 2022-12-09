/**
 * 
 * @author 최진실
 *
 */
package com.rence.dashboard.repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.rence.backoffice.common.OperatingTime;
import com.rence.backoffice.model.BackOfficeOperatingTimeEntity;
import com.rence.backoffice.model.BackOfficeOperatingTimeVO;
import com.rence.backoffice.model.BackOfficeVO;
import com.rence.backoffice.repository.BackOfficeOperatingTimeRepository;
import com.rence.backoffice.repository.BackOfficeOperatingTimeSelectRepository;
import com.rence.backoffice.repository.BackOfficeRepository;
import com.rence.dashboard.model.BOMileageVO;
import com.rence.dashboard.model.BOPaymentVO;
import com.rence.dashboard.model.CommentInsertVO;
import com.rence.dashboard.model.CommentListAView;
import com.rence.dashboard.model.CommentListQView;
import com.rence.dashboard.model.CommentSummaryView;
import com.rence.dashboard.model.CommentVO;
import com.rence.dashboard.model.ReservationView;
import com.rence.dashboard.model.ReserveListView;
import com.rence.dashboard.model.ReserveSummaryView;
import com.rence.dashboard.model.ReviewListView;
import com.rence.dashboard.model.RoomInsertVO;
import com.rence.dashboard.model.RoomSummaryView;
import com.rence.dashboard.model.RoomVO;
import com.rence.dashboard.model.SalesSettlementDetailView;
import com.rence.dashboard.model.SalesSettlementSummaryView;
import com.rence.dashboard.model.SalesSettlementViewVO;
import com.rence.dashboard.model.ScheduleEntity;
import com.rence.dashboard.model.ScheduleListView;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DashBoardDAOImpl implements DashBoardDAO {

	@Autowired
	ReserveRepository rv_repository;

	@Autowired
	ReserveSummaryRepository r_summary_repository;

	@Autowired
	RoomRepository rm_repository;

	@Autowired
	RoomInsertRepository rm_info_repository;

	@Autowired
	BackOfficeRepository b_repository;

	@Autowired
	CommentRepository c_repository;

	@Autowired
	CommentSummaryRepository c_summary_repository;

	@Autowired
	CommentInsertRepository c_insert_repository;

	@Autowired
	ReviewRepository r_repository;

	@Autowired
	SalesSettlementRepository s_repository;

	@Autowired
	BackOfficeOperatingTimeRepository o_repository;

	@Autowired
	CommentQListRepository cq_repository;

	@Autowired
	BackOfficeOperatingTimeSelectRepository bos_repository;

	@Autowired
	ReserveAutoUpdateRepository reserveAutoUpdateRepository;

	@Autowired
	CommentAListRepository ca_repository;

	@Autowired
	RoomSummaryRepository rm_summary_repository;

	@Autowired
	SalesSettlementSummaryRepository ss_summary_repository;

	@Autowired
	SalesSettlementDetailRepository s_detail_repository;

	@Autowired
	ScheduleListRepository sc_repository;

	@Autowired
	ReservationRepository reservation_repository;

	@Autowired
	ScheduleRepository schedule_repository;

	@Autowired
	PaymentCancelRepository p_repository;

	@Autowired
	SalesMileageRepository m_repository;
	
	@Autowired
	OperatingTime operatingTime;

	/**
	 * 대쉬보드 메인 - 예약 요약
	 */
	@Override
	public List<ReserveSummaryView> reserve_summary_selectAll(String backoffice_no) {
		return r_summary_repository.reserve_summary_selectAll(backoffice_no);
	}

	/**
	 * 대쉬보드 메인 - 문의 요약
	 */
	@Override
	public List<CommentSummaryView> comment_summary_selectAll(String backoffice_no) {
		return c_summary_repository.comment_summary_selectAll(backoffice_no);
	}

	/**
	 * 대쉬보드 메인 - 정산 요약
	 */
	@Override
	public SalesSettlementSummaryView payment_summary_selectOne(String backoffice_no) {
		SalesSettlementSummaryView ss = new SalesSettlementSummaryView();

		Integer pay_before_desk_meeting = ss_summary_repository.select_pay_before_desk_meeting_sum(backoffice_no);
		Integer pay_after_desk_meeting_deposit = ss_summary_repository
				.select_pay_after_desk_meeting_deposit_sum(backoffice_no);
		Integer pay_after_desk_meeting_balance = ss_summary_repository
				.select_pay_after_desk_meeting_balance_sum(backoffice_no);
		Integer pay_office = ss_summary_repository.select_pay_office_sum(backoffice_no);

		int sales_total = pay_before_desk_meeting + pay_after_desk_meeting_deposit + pay_after_desk_meeting_balance
				+ pay_office;

		ss.setSales_total(String.valueOf(sales_total));

		Integer pay_cancel = ss_summary_repository.select_pay_cancel(backoffice_no);

		int sales_cancel = pay_cancel;

		ss.setSales_cancel(String.valueOf(sales_cancel));

		ss.setSales_income(String.valueOf(sales_total - sales_cancel));

		return ss;
	}

	/**
	 * 대쉬보드 메인 - 공간 요약
	 */
	@Override
	public RoomSummaryView room_summary_selectOne(String backoffice_no) {
		RoomSummaryView rs = new RoomSummaryView();

		Float review_point = rm_summary_repository.select_avg_review_point(backoffice_no);
		if (review_point == null) {
			rs.setReview_point((float) 0.0);
		} else {
			rs.setReview_point(review_point);
		}
		Integer comment_no = rm_summary_repository.select_comment_cnt(backoffice_no);
		if (comment_no == null) {
			rs.setComment_no(0);
		} else {
			rs.setComment_no(comment_no);
		}
		Integer review_no = rm_summary_repository.select_review_cnt(backoffice_no);
		if (review_no == null) {
			rs.setReview_no(0);
		} else {
			rs.setReview_no(review_no);
		}
		Integer reserve_no = rm_summary_repository.select_reserve_cnt(backoffice_no);
		if (reserve_no == null) {
			rs.setReserve_no(0);
		} else {
			rs.setReserve_no(reserve_no);
		}

		return rs;
	}

	/**
	 * 공간관리 - 공간 리스트
	 */
	@Override
	public List<RoomVO> dashboard_room_list(String backoffice_no, Integer page) { 
		log.info("reserve_summary_selectAll().....");
		log.info("current page: {}", page);

		Integer row_count = 12;
		Integer start_row = (page - 1) * row_count + 1;
		Integer end_row = page * row_count;
		log.info("start_row: " + start_row);
		log.info("end_row: " + end_row);

		return rm_repository.selectAll_room_list(backoffice_no, start_row, end_row);
	}

	/**
	 * 공간관리 - 공간 추가, 수정 팝업(백오피스 정보(타입) 가져오기)
	 */
	@Override
	public BackOfficeVO select_one_backoffice_info(String backoffice_no) {
		return b_repository.select_one_backoffice_info(backoffice_no);
	}

	/**
	 * 공간관리 - 공간 추가 처리
	 */
	@Override
	public int backoffice_insertOK_room(String backoffice_no, RoomInsertVO rvo) {
		if (rvo.getRoom_type().equals("desk")) {
			rvo.setRoom_price(10000);
		} else if (rvo.getRoom_type().equals("meeting_04")) {
			rvo.setRoom_price(20000);
		} else if (rvo.getRoom_type().equals("meeting_06")) {
			rvo.setRoom_price(30000);
		} else if (rvo.getRoom_type().equals("meeting_10")) {
			rvo.setRoom_price(50000);
		}
		rvo.setBackoffice_no(backoffice_no);

		return rm_info_repository.backoffice_insertOK_room(rvo, rvo.getRoom_price());
	}

	/**
	 * 공간관리 - 공간 수정 팝업(공간 정보 가져오기)
	 */
	@Override
	public RoomVO select_one_room_info(String backoffice_no, String room_no) {
		return rm_repository.select_one_room_info(backoffice_no, room_no);
	}

	/**
	 * 공간관리 - 공간 수정 처리
	 */
	@Override
	public int backoffice_updateOK_room(String backoffice_no, RoomInsertVO rvo) {
		rvo.setBackoffice_no(backoffice_no);
		if (rvo.getRoom_type().equals("desk")) {
			rvo.setRoom_price(10000);
		} else if (rvo.getRoom_type().equals("meeting_04")) {
			rvo.setRoom_price(20000);
		} else if (rvo.getRoom_type().equals("meeting_06")) {
			rvo.setRoom_price(30000);
		} else if (rvo.getRoom_type().equals("meeting_10")) {
			rvo.setRoom_price(50000);
		}
		return rm_info_repository.backoffice_updateOK_room(rvo, rvo.getRoom_price());
	}

	/**
	 * 공간관리 - 공간 삭제
	 */
	@Override
	public void backoffice_deleteOK_room(String backoffice_no, String room_no) {
		rm_repository.backoffice_deleteOK_room(backoffice_no, room_no);
	}
	
	/**
	 * 공간관리 -문의(리스트)
	 */
	@Override
	public List<CommentListQView> backoffice_qna_selectAll(String backoffice_no, Integer page) {
		log.info("backoffice_qna_selectAll().....");
		log.info("currentpage:{}", page);

		Integer row_count = 10;
		Integer start_row = (page - 1) * row_count + 1;
		Integer end_row = page * row_count;
		
		List<CommentListQView> qvos = cq_repository.select_all_q(backoffice_no, start_row, end_row);
		log.info("Question:{}", qvos);
		log.info("Question::::{}", qvos.size());
		if (qvos != null) {
			for (int i = 0; i < qvos.size(); i++) {
				CommentListAView ca_vo = new CommentListAView();
				ca_vo.setMother_no(qvos.get(i).getComment_no());
				String mother_no = ca_vo.getMother_no();
				log.info("mother_no::::{}", mother_no);
				List<CommentListAView> avos = ca_repository.select_all_a(backoffice_no, mother_no);
				log.info("Answer:{}", avos);

				if (avos != null) {
					for (int j = 0; j < avos.size(); j++) {

						qvos.get(i).setAnswer_no(avos.get(j).getComment_no());
						qvos.get(i).setAnswer_content(avos.get(j).getComment_content());
						qvos.get(i).setAnswer_date(avos.get(j).getComment_date());
					}
				}
			}
		}
		log.info("Question&+Answer:{}", qvos);

		return qvos;

	}
	
	/**
	 * 공간관리 - 답변 작성 팝업
	 */
	@Override
	public CommentVO backoffice_insert_comment(String backoffice_no, String room_no, String comment_no) {
		return c_repository.backoffice_insert_comment(backoffice_no, room_no, comment_no);
	}
	
	/**
	 * 공간관리 - 답변 작성
	 */
	@Override
	public int backoffice_insertOK_comment(CommentInsertVO cvo) {
		return c_insert_repository.backoffice_insertOK_comment(cvo, cvo.getComment_date());
	}

	/**
	 * 공간관리 - 답변 작성 후, 해당 문의 답변 상태 변경
	 */
	@Override
	public int update_comment_state_T(String backoffice_no, String comment_no) {
		return c_insert_repository.update_comment_state_T(backoffice_no, comment_no);
	}
	
	/**
	 * 공간관리 - 답변 삭제
	 */
	@Override
	public int backoffice_deleteOK_comment(String backoffice_no, String comment_no) {
		return c_insert_repository.backoffice_deleteOK_comment(backoffice_no, comment_no);
	}

	/**
	 * 공간관리 - 답변 삭제 후, 해당 문의 답변 상태 변경
	 */
	@Override
	public int update_comment_state_F(String backoffice_no, String mother_no) {
		return c_insert_repository.update_comment_state_F(backoffice_no, mother_no);
	}
	
	/**
	 * 공간관리 - 리뷰 (리스트)
	 */
	@Override
	public List<ReviewListView> backoffice_review_selectAll(String backoffice_no, Integer page) {
		log.info("backoffice_review_selectAll().....");
		log.info("currentpage:{}", page);

		Integer row_count = 6;
		Integer start_row = (page - 1) * row_count + 1;
		Integer end_row = page * row_count;

		return r_repository.backoffice_review_selectAll(backoffice_no, start_row, end_row);
	}
	
	/**
	 * 예약 관리 - 리스트
	 */
	@Override
	public List<ReserveListView> backoffice_reserve_selectAll(String backoffice_no, String reserve_state, int start_row,
			int end_row) {
		log.info("backoffice_reserve_selectAll().....");
		log.info("reserve_state: {}.....", reserve_state);
		log.info("start_row: {}.....", start_row);
		log.info("end_row: {}.....", end_row);

		List<ReserveListView> reserve = null;

		if (reserve_state.equals("all")) {
			reserve = rv_repository.backoffice_reserve_selectAll(backoffice_no, start_row, end_row);
		} else if (reserve_state.equals("in_use")) {
			reserve = rv_repository.backoffice_reserve_selectAll_inuse(backoffice_no, start_row, end_row);
		} else if (reserve_state.equals("end")) {
			reserve = rv_repository.backoffice_reserve_selectAll_end(backoffice_no, start_row, end_row);
		} else if (reserve_state.equals("cancel")) {
			reserve = rv_repository.backoffice_reserve_selectAll_cancel(backoffice_no, start_row, end_row);
		}

		log.info("reserve : {}", reserve);
		return reserve;
	}
	

	/**
	 * 예약 관리 - 리스트(검색)
	 */
	@Override
	public List<ReserveListView> backoffice_search_reserve(String backoffice_no, String searchword,
			String reserve_state, int start_row, int end_row) {
		log.info("reserve_state: {}.....", reserve_state);
		log.info("start_row: {}.....", start_row);
		log.info("end_row: {}.....", end_row);
		log.info("searchword: {}.....", searchword);

		List<ReserveListView> reserve = null;

		if (reserve_state.equals("all")) {
			reserve = rv_repository.backoffice_reserve_selectAll_search(backoffice_no, start_row, end_row,
					"%" + searchword + "%");
			log.info("search list :: {}", reserve);
		} else if (reserve_state.equals("in_use")) {
			reserve = rv_repository.backoffice_reserve_selectAll_inuse_search(backoffice_no, start_row, end_row,
					"%" + searchword + "%");
		} else if (reserve_state.equals("end")) {
			reserve = rv_repository.backoffice_reserve_selectAll_end_search(backoffice_no, start_row, end_row,
					"%" + searchword + "%");
		} else if (reserve_state.equals("cancel")) {
			reserve = rv_repository.backoffice_reserve_selectAll_cancel_search(backoffice_no, start_row, end_row,
					"%" + searchword + "%");
		}

		log.info("reserve : {}", reserve);
		return reserve;
	}
	
	/**
	 * 정산 관리 - 리스트(일일/주간/월간)
	 */
	@Override
	public SalesSettlementDetailView backoffice_sales_selectOne(String backoffice_no, String sales_date) {
		SalesSettlementDetailView ss = new SalesSettlementDetailView();

		if (sales_date.equals("day")) {
			// 금일
			Integer pay_before_desk_meeting = s_detail_repository.select_pay_before_desk_meeting_sum(backoffice_no);
			Integer pay_after_desk_meeting_deposit = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum(backoffice_no);
			Integer pay_after_desk_meeting_balance = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum(backoffice_no);
			Integer pay_office = s_detail_repository.select_pay_office_sum(backoffice_no);

			int sales_total = pay_before_desk_meeting + pay_after_desk_meeting_deposit + pay_after_desk_meeting_balance
					+ pay_office;

			ss.setSales_total(String.valueOf(sales_total));

			Integer pay_cancel = s_detail_repository.select_pay_cancel(backoffice_no);

			int sales_cancel = pay_cancel;

			ss.setSales_cancel(String.valueOf(sales_cancel));

			ss.setSales_income(String.valueOf(sales_total - sales_cancel));

			// 전일
			Integer pay_before_desk_meeting_pre = s_detail_repository
					.select_pay_before_desk_meeting_sum_pre(backoffice_no);
			Integer pay_after_desk_meeting_deposit_pre = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum_pre(backoffice_no);
			Integer pay_after_desk_meeting_balance_pre = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum_pre(backoffice_no);
			Integer pay_office_pre = s_detail_repository.select_pay_office_sum_pre(backoffice_no);

			int pre_sales_total = pay_before_desk_meeting_pre + pay_after_desk_meeting_deposit_pre
					+ pay_after_desk_meeting_balance_pre + pay_office_pre;

			ss.setPre_sales_total(String.valueOf(pre_sales_total));

			Integer pre_pay_cancel = s_detail_repository.select_pay_cancel_pre(backoffice_no);

			int pre_sales_cancel = pre_pay_cancel;

			ss.setPre_sales_cancel(String.valueOf(pre_sales_cancel));

			ss.setPre_sales_income(String.valueOf(pre_sales_total - pre_sales_cancel));

			ss.setSales_gap(String.valueOf((sales_total - sales_cancel) - (pre_sales_total - pre_sales_cancel)));
		} else if (sales_date.equals("week")) {
			// 금주
			Integer pay_before_desk_meeting = s_detail_repository
					.select_pay_before_desk_meeting_sum_week(backoffice_no);
			Integer pay_after_desk_meeting_deposit = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum_week(backoffice_no);
			Integer pay_after_desk_meeting_balance = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum_week(backoffice_no);
			Integer pay_office = s_detail_repository.select_pay_office_sum_week(backoffice_no);

			int sales_total = pay_before_desk_meeting + pay_after_desk_meeting_deposit + pay_after_desk_meeting_balance
					+ pay_office;

			ss.setSales_total(String.valueOf(sales_total));

			Integer pay_cancel = s_detail_repository.select_pay_cancel_week(backoffice_no);

			int sales_cancel = pay_cancel;

			ss.setSales_cancel(String.valueOf(sales_cancel));

			ss.setSales_income(String.valueOf(sales_total - sales_cancel));

			// 전주
			Integer pay_before_desk_meeting_pre = s_detail_repository
					.select_pay_before_desk_meeting_sum_pre_week(backoffice_no);
			Integer pay_after_desk_meeting_deposit_pre = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum_pre_week(backoffice_no);
			Integer pay_after_desk_meeting_balance_pre = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum_pre_week(backoffice_no);
			Integer pay_office_pre = s_detail_repository.select_pay_office_sum_pre_week(backoffice_no);

			int pre_sales_total = pay_before_desk_meeting_pre + pay_after_desk_meeting_deposit_pre
					+ pay_after_desk_meeting_balance_pre + pay_office_pre;

			ss.setPre_sales_total(String.valueOf(pre_sales_total));

			Integer pre_pay_cancel = s_detail_repository.select_pay_cancel_pre_week(backoffice_no);

			int pre_sales_cancel = pre_pay_cancel;

			ss.setPre_sales_cancel(String.valueOf(pre_sales_cancel));

			ss.setPre_sales_income(String.valueOf(pre_sales_total - pre_sales_cancel));

			ss.setSales_gap(String.valueOf((sales_total - sales_cancel) - (pre_sales_total - pre_sales_cancel)));
		} else if (sales_date.equals("month")) {
			// 당월
			Integer pay_before_desk_meeting = s_detail_repository
					.select_pay_before_desk_meeting_sum_month(backoffice_no);
			Integer pay_after_desk_meeting_deposit = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum_month(backoffice_no);
			Integer pay_after_desk_meeting_balance = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum_month(backoffice_no);
			Integer pay_office = s_detail_repository.select_pay_office_sum_month(backoffice_no);

			int sales_total = pay_before_desk_meeting + pay_after_desk_meeting_deposit + pay_after_desk_meeting_balance
					+ pay_office;

			ss.setSales_total(String.valueOf(sales_total));

			Integer pay_cancel = s_detail_repository.select_pay_cancel_month(backoffice_no);

			int sales_cancel = pay_cancel;

			ss.setSales_cancel(String.valueOf(sales_cancel));

			ss.setSales_income(String.valueOf(sales_total - sales_cancel));

			// 전월
			Integer pay_before_desk_meeting_pre = s_detail_repository
					.select_pay_before_desk_meeting_sum_pre_month(backoffice_no);
			Integer pay_after_desk_meeting_deposit_pre = s_detail_repository
					.select_pay_after_desk_meeting_deposit_sum_pre_month(backoffice_no);
			Integer pay_after_desk_meeting_balance_pre = s_detail_repository
					.select_pay_after_desk_meeting_balance_sum_pre_month(backoffice_no);
			Integer pay_office_pre = s_detail_repository.select_pay_office_sum_pre_month(backoffice_no);

			int pre_sales_total = pay_before_desk_meeting_pre + pay_after_desk_meeting_deposit_pre
					+ pay_after_desk_meeting_balance_pre + pay_office_pre;

			ss.setPre_sales_total(String.valueOf(pre_sales_total));

			Integer pre_pay_cancel = s_detail_repository.select_pay_cancel_pre_month(backoffice_no);

			int pre_sales_cancel = pre_pay_cancel;

			ss.setPre_sales_cancel(String.valueOf(pre_sales_cancel));

			ss.setPre_sales_income(String.valueOf(pre_sales_total - pre_sales_cancel));

			ss.setSales_gap(String.valueOf((sales_total - sales_cancel) - (pre_sales_total - pre_sales_cancel)));
		}
		return ss;
	}
	
	/**
	 * 정산 관리 - 리스트(정산 내역)
	 */
	@Override
	public List<SalesSettlementViewVO> backoffice_sales_selectAll(String backoffice_no, Integer page) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 정산 관리 - 정산 상태 변경
	 */
	@Override
	public int backoffice_updateOK_sales(String backoffice_no, String room_no, String payment_no) {
		int flag = 0;
		BOPaymentVO pvo = new BOPaymentVO();
		
		pvo = p_repository.select_paymentinfo_user_no(payment_no); // 결제정보 테이블에서 user_no 정보 얻기
		String user_no = pvo.getUser_no();

		BOMileageVO mvo = m_repository.backoffice_select_mileage_total(user_no); // 1. 사용자의 마지막 mileage_total
		BOMileageVO mvo2 = m_repository.backoffice_select_mileage_w(user_no, payment_no); // 2. 적립 예정 마일리지

		if (mvo2.getMileage_change() != 0) { // 선결제
			int mileage_change = mvo2.getMileage_change(); // 2
			int mileage_total = mvo.getMileage_total() + mileage_change; // 1+2

			flag = m_repository.backoffice_insert_mileage_state_t(mileage_total, user_no, mileage_change, payment_no); // 마일리지 적립
		} else { // 후결제
			flag = s_repository.backoffice_update_mileage_state_c(payment_no); // change 가 0인 mileage 는 C로 상태 변경
		}
		if (flag == 1) {
			s_repository.backoffice_updateOK_sales_state_t(backoffice_no, room_no, payment_no); // 결제 정보 테이블의 정산 상태 변경
		}
		return flag;
	}
	
	/**
	 * 환경설정 - 환경 설정 페이지, 정보 수정 페이지
	 */
	@Override
	public BackOfficeVO backoffice_setting_selectOne(BackOfficeVO bvo) {
		return b_repository.backoffice_setting_selectOne(bvo.getBackoffice_no());
	}

	/**
	 * 환경설정 - 비밀번호 수정
	 */
	@Override
	public BackOfficeVO backoffice_select_pw(BackOfficeVO bvo) {
		return b_repository.backoffice_select_pw(bvo.getBackoffice_no());
	}
	
	/**
	 * 환경설정 - 업체 탈퇴 요청 (백오피스 삭제)
	 */
	@Override
	public int backoffice_setting_delete(BackOfficeVO bvo) {
		return b_repository.update_backoffice_state_o(bvo.getBackoffice_no());
	}

	/**
	 * 환경설정 - 업체 탈퇴 요청 (공간 삭제)
	 */
	@Override
	public int backoffice_room_deleteALL(BackOfficeVO bvo) {
		return b_repository.backoffice_room_deleteALL(bvo.getBackoffice_no());
	}
	
	/**
	 * 환경설정 - 정보 변경 (운영시간 정보 가져오기)
	 */
	@Override
	public BackOfficeOperatingTimeVO backoffice_setting_selectOne_operatingtime(String backoffice_no) {
		return bos_repository.backoffice_setting_selectOne_operatingtime(backoffice_no);
	}
	
	/**
	 * 환경설정 - 정보 변경 처리 (업체 정보 업데이트)
	 */
	@Override
	public int backoffice_updateOK_host(BackOfficeVO bvo) {
		return b_repository.backoffice_updateOK_host(bvo);
	}

	/**
	 * 환경설정 - 정보 변경 처리 (운영시간 업데이트)
	 */
	@Override
	public int backoffice_updateOK_opt(BackOfficeOperatingTimeVO ovo) {
		BackOfficeOperatingTimeEntity ovo2 = operatingTime.operatingTime(ovo);
		return o_repository.backoffice_updateOK_opt(ovo2);
	}
	
	/**
	 * 일정 관리 - 날짜, 시간 선택 후
	 */
	@Override
	public List<ScheduleListView> backoffice_schedule_list(String backoffice_no, String not_sdate, String not_edate,
			String not_stime, String not_etime, String off_type) {
		ScheduleListView sc = new ScheduleListView();

		// 1, 2 날짜 형태 변환
		String reserve_stime = null;
		String reserve_etime = null;
		if (off_type.equals("dayoff")) { // 휴무일 때
			log.info("휴무");
			
			not_stime = "00:00:00";
			not_etime = "23:59:59";
			
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		} else { // 브레이크 타임일 때
			log.info("브레이크타임");

			log.info("not_sdate : {} ", not_sdate);
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);

			not_edate = (not_sdate);
			log.info("not_edate : {} ", not_edate);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		}

		// 1.해당 날짜, 시간에 예약이 있는 리스트
		List<ScheduleListView> sc_vos_o = sc_repository.backoffice_schedule_list(backoffice_no, reserve_stime, reserve_etime);
		log.info("sc_vos_o : {} ", sc_vos_o.size());

		// 2.백오피스가 가진 모든 공간 리스트
		List<ScheduleListView> sc_vos_x = sc_repository.backoffice_schedule_list_All(backoffice_no);
		log.info("sc_vos_x : {} ", sc_vos_x);
		log.info("sc_vos_x : {} ", sc_vos_x.size());
		
		// 3.휴무, 브레이크 타임이 설정된 공간 리스트 
		not_stime = not_sdate + not_stime;
		not_etime = not_edate + not_etime;
		log.info("not_stime : {} ", not_stime);
		log.info("not_etime : {} ", not_etime);
		List<ScheduleEntity> off_list = schedule_repository.backoffice_schedule_list_exist_off(backoffice_no, not_stime,
				not_etime);
		log.info("off_list : {} ", off_list);
		log.info("off_list : {} ", off_list.size());

		List<ScheduleListView> sc_vos = new ArrayList<ScheduleListView>();

		if (sc_vos_x != null) {
			// 2 - 1 (중복 제거)
			if (sc_vos_o != null) {

				for (int j = 0; j < sc_vos_o.size(); j++) {
					String room_no = sc_vos_o.get(j).getRoom_no();
					Predicate<ScheduleListView> condition = str -> str.getRoom_no().equals(room_no);
					sc_vos_x.removeIf(condition);
				}
			}
			log.info("sc_vos_x - sc_vos_o : {} ", sc_vos_x.size());

			// 공간 예약 상태 설정
			for (ScheduleListView scvo : sc_vos_o) {
				scvo.setReserve_is("O");
			}

			for (ScheduleListView scvo : sc_vos_x) {
				scvo.setReserve_is("X");
				scvo.setReserve_cnt(0);
			}

			// 2 -3(휴무, 브레이크 타임이 설정된 공간 제외)
			if (off_list != null && sc_vos_x != null) {
				for (int j = 0; j < off_list.size(); j++) {
					String room_no = off_list.get(j).getRoom_no(); 
					Predicate<ScheduleListView> condition = str -> str.getRoom_no().equals(room_no);
					sc_vos_x.removeIf(condition);
				}
			}

			log.info("sc_vos_x - off_list : {} ", sc_vos_x.size());

			sc_vos.addAll(sc_vos_o);
			sc_vos.addAll(sc_vos_x);

		} else {
			sc_vos = null;
		}

		log.info("sc_vos : {} ", sc_vos.size());

		return sc_vos;
	}
	
	/**
	 * 일정 관리 - 날짜, 시간 선택 후, 휴무, 브레이크타임 설정
	 */
	@Override
	public int backoffice_schedueOK(String backoffice_no, String not_stime, String not_etime, String room_no) {
		return schedule_repository.backoffice_schedueOK(backoffice_no, not_stime, not_etime, room_no);
	}
	
	/**
	 * 일정 관리 - 해당 날짜, 시간에 예약자 리스트
	 */
	@Override
	public List<ReservationView> backoffice_reservation(String backoffice_no, String not_sdate, String not_edate,
			String not_stime, String not_etime, String room_no, String off_type, int min, int max) {
		String reserve_stime = null;
		String reserve_etime = null;
		if (off_type.equals("dayoff")) { // 휴무일 때
			log.info("휴무");
			
			not_stime = "00:00:00";
			not_etime = "23:59:59";
			
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		} else { // 브레이크 타임일 때
			log.info("브레이크 타임");
			
			log.info("not_sdate : {} ", not_sdate);
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);

			not_edate = (not_sdate);
			log.info("not_edate : {} ", not_edate);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		}

		List<ReservationView> rv_vos = reservation_repository.backoffice_reservation_list(backoffice_no, reserve_stime,
				reserve_etime, room_no, min, max);

		return rv_vos;
	}
	
	/**
	 * 일정 관리 - 예약취소
	 * 
	 */
	@Override
	public BOPaymentVO backoffice_reservation_cancel(String backoffice_no, String reserve_no, String user_no) {
		int flag = reserveAutoUpdateRepository.update_reserve_state_cancel(reserve_no);
		BOPaymentVO pvo = new BOPaymentVO();
		// 결제 취소,
		if (flag == 1) {
			// 결제정보 테이블의 상태 'C' 로 변경
			p_repository.backoffice_update_payment_state_host_cancel(reserve_no); // 환불 상태 'C', 환불 금액 = 실제 결제 금액, 결제일시 = 환불일시
			pvo = p_repository.select_paymentinfo(reserve_no); // 결제 정보
			String payment_no = pvo.getPayment_no();

			BOMileageVO mvo = m_repository.backoffice_select_mileage_total(user_no); // 1. 사용자의 마지막 mileage_total
			BOMileageVO mvo2 = m_repository.backoffice_select_mileage_f(user_no, payment_no); // 2. 사용자가 사용한 마일리지

			if (mvo2 != null) { // 사용한 마일리지가 있으면
				int mileage_change = mvo2.getMileage_change(); // 2
				int mileage_total = mvo.getMileage_total() + mileage_change; // 1 + 2

				m_repository.backoffice_insert_mileage_state_t(mileage_total, user_no, mileage_change, payment_no); // 마일리지 재적립
			}

			s_repository.backoffice_update_cancel_mileage_state_c(reserve_no); // w 상태의 마일리지 -> c 상태로 변경
		}
		return pvo;
	}

	/**
	 * 일정 관리 - 예약취소 (회사명 가져오기)
	 * 
	 */
	@Override
	public BackOfficeVO backoffice_select_companyname(String backoffice_no) {
		return b_repository.select_one_backoffice_info(backoffice_no);
	}
	
	/**
	 * 일정 관리 - 백오피스 휴무 일정
	 */
	@Override
	public List<ScheduleEntity> backoffice_schedule_calendar(String backoffice_no) {
		return schedule_repository.backoffice_schedule_calendar(backoffice_no);
	}

	/**
	 * 일정 관리 - 백오피스 휴무 일정
	 */
	@Override
	public RoomInsertVO backoffice_schedule_calendar_room_name(String room_no) {
		return rm_info_repository.backoffice_schedule_calendar_room_name(room_no);
	}

	/**
	 * 일정 관리 - 휴무/브레이크타임 취소
	 */
	@Override
	public int backoffice_schedule_cancel(String backoffice_no, String schedule_no) {
		return schedule_repository.backoffice_schedule_cancel(backoffice_no, schedule_no);
	}
	

	// ******** 페이징 ***********
	// 공간 리스트
	public long dashboard_room_list_cnt(String backoffice_no) {
		return rm_repository.dashboard_room_list_cnt(backoffice_no);
	}

	// 문의 리스트
	public long backoffice_qna_selectAll_cnt(String backoffice_no) {
		return cq_repository.select_all_q_cnt(backoffice_no);
	}

	// 후기 리스트
	public long backoffice_review_selectAll_cnt(String backoffice_no) {
		return r_repository.backoffice_review_selectAll_cnt(backoffice_no);
	}

	// 정산 리스트
	public long backoffice_sales_selectAll_cnt(String backoffice_no) {
		return s_repository.backoffice_sales_selectAll_cnt(backoffice_no);
	}

	// 예약자 리스트
	public int backoffice_reservation_cnt(String backoffice_no, String not_sdate, String not_edate, String not_stime,
			String not_etime, String room_no, String off_type) {
		String reserve_stime = null;
		String reserve_etime = null;
		if (off_type.equals("dayoff")) { // 휴무일 때
			log.info("휴무");
			
			not_stime = "00:00:00";
			not_etime = "23:59:59";
			
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		} else { // 브레이크 타임일 때
			log.info("브레이크 타임");
			
			log.info("not_sdate : {} ", not_sdate);
			reserve_stime = (not_sdate + not_stime);
			log.info("reserve_stime : {} ", reserve_stime);

			not_edate = (not_sdate);
			log.info("not_edate : {} ", not_edate);
			reserve_etime = (not_edate + not_etime);
			log.info("reserve_etime : {} ", reserve_etime);
		}

		int total_cnt = reservation_repository.backoffice_reservation_list_cnt(backoffice_no, reserve_stime,
				reserve_etime, room_no);
		return total_cnt;
	}

	// 예약 관리 리스트
	public int backoffice_reserve_selectAll_cnt(String backoffice_no, String reserve_state) {
		log.info("backoffice_reserve_selectAll_cnt().....");
		log.info("reserve_state: {}.....", reserve_state);

		int total_cnt = 0;

		if (reserve_state.equals("all")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll(backoffice_no);
		} else if (reserve_state.equals("in_use")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_inuse(backoffice_no);
		} else if (reserve_state.equals("end")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_end(backoffice_no);
		} else if (reserve_state.equals("cancel")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_cancel(backoffice_no);
		}

		return total_cnt;
	}

	// 예약 관리 리스트 - 검색
	public int backoffice_search_reserve_cnt(String backoffice_no, String searchword, String reserve_state) {
		int total_cnt = 0;

		if (reserve_state.equals("all")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_search(backoffice_no, "%" + searchword + "%");
		} else if (reserve_state.equals("in_use")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_inuse_search(backoffice_no, "%" + searchword + "%");
		} else if (reserve_state.equals("end")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_end_search(backoffice_no, "%" + searchword + "%");
		} else if (reserve_state.equals("cancel")) {
			total_cnt = rv_repository.backoffice_reserve_selectAll_cancel_search(backoffice_no, "%" + searchword + "%");
		}
		log.info("total_cnt:::::{}",total_cnt);
		
		return total_cnt;
	}

}
