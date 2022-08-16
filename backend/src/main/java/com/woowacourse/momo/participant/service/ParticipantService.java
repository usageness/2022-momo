package com.woowacourse.momo.participant.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.woowacourse.momo.group.domain.group.Group;
import com.woowacourse.momo.group.service.GroupFindService;
import com.woowacourse.momo.member.domain.Member;
import com.woowacourse.momo.member.service.MemberFindService;
import com.woowacourse.momo.member.service.dto.response.MemberResponse;
import com.woowacourse.momo.member.service.dto.response.MemberResponseAssembler;
import com.woowacourse.momo.participant.domain.ParticipantRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ParticipantService {

    private final MemberFindService memberFindService;
    private final GroupFindService groupFindService;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void participate(Long groupId, Long memberId) {
        Group group = groupFindService.findGroup(groupId);
        Member member = memberFindService.findMember(memberId);

        group.participate(member);
    }

    public List<MemberResponse> findParticipants(Long groupId) {
        Group group = groupFindService.findGroup(groupId);
        List<Member> participants = group.getParticipants();

        return MemberResponseAssembler.memberResponses(participants);
    }

    @Transactional
    public void delete(Long groupId, Long memberId) {
        validateLeave(groupId, memberId);
        participantRepository.deleteByGroupIdAndMemberId(groupId, memberId);
    }

    private void validateLeave(Long groupId, Long memberId) {
        Group group = groupFindService.findGroup(groupId);
        Member member = memberFindService.findMember(memberId);
        group.validateLeave(member);
    }
}
