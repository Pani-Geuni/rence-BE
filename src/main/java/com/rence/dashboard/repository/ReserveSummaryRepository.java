/**
 * 
 * @author 최진실
 *
 */
package com.rence.dashboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rence.dashboard.model.ReserveSummaryViewEntity;


public interface ReserveSummaryRepository extends JpaRepository<ReserveSummaryViewEntity, Object> { // 예약 요약

	@Query(nativeQuery = true, value="   select reserve_no, TO_CHAR(reserve_sdate, 'YYYY-MM-DD HH24:MI:SS') as reserve_sdate , TO_CHAR(reserve_edate, 'YYYY-MM-DD HH24:MI:SS') as reserve_edate, room_name, user_name, actual_payment, reserve_state, backoffice_no"
			+ " from( select r.reserve_no, r.reserve_sdate, r.reserve_edate, room_name, p.actual_payment, r.reserve_state, r.backoffice_no , user_name"
			+ " from reserveinfo r , paymentinfo p, roominfo rm, backofficeinfo b, userinfo u where r.reserve_no=p.reserve_no and p.room_no = rm.room_no and rm.backoffice_no = b.backoffice_no and r.user_no = u.user_no and r.reserve_state!='false' and r.backoffice_no=?1 order by reserve_sdate desc)"
			+ " where  ROWNUM <= 5")
	public List<ReserveSummaryViewEntity> reserve_summary_selectAll(String backoffice_no);

	

}
