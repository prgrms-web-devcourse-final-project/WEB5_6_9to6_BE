package com.grepp.studium.member.service;

import com.grepp.studium.alarm_recipient.domain.AlarmRecipient;
import com.grepp.studium.alarm_recipient.repos.AlarmRecipientRepository;
import com.grepp.studium.member.domain.Member;
import com.grepp.studium.member.model.MemberDTO;
import com.grepp.studium.member.repos.MemberRepository;
import com.grepp.studium.own_item.domain.OwnItem;
import com.grepp.studium.own_item.repos.OwnItemRepository;
import com.grepp.studium.study_applicant.domain.StudyApplicant;
import com.grepp.studium.study_applicant.repos.StudyApplicantRepository;
import com.grepp.studium.study_member.domain.StudyMember;
import com.grepp.studium.study_member.repos.StudyMemberRepository;
import com.grepp.studium.util.NotFoundException;
import com.grepp.studium.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final AlarmRecipientRepository alarmRecipientRepository;
    private final StudyApplicantRepository studyApplicantRepository;
    private final OwnItemRepository ownItemRepository;

    public MemberService(final MemberRepository memberRepository,
            final StudyMemberRepository studyMemberRepository,
            final AlarmRecipientRepository alarmRecipientRepository,
            final StudyApplicantRepository studyApplicantRepository,
            final OwnItemRepository ownItemRepository) {
        this.memberRepository = memberRepository;
        this.studyMemberRepository = studyMemberRepository;
        this.alarmRecipientRepository = alarmRecipientRepository;
        this.studyApplicantRepository = studyApplicantRepository;
        this.ownItemRepository = ownItemRepository;
    }

    public List<MemberDTO> findAll() {
        final List<Member> members = memberRepository.findAll(Sort.by("memberId"));
        return members.stream()
                .map(member -> mapToDTO(member, new MemberDTO()))
                .toList();
    }

    public MemberDTO get(final Integer memberId) {
        return memberRepository.findById(memberId)
                .map(member -> mapToDTO(member, new MemberDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MemberDTO memberDTO) {
        final Member member = new Member();
        mapToEntity(memberDTO, member);
        return memberRepository.save(member).getMemberId();
    }

    public void update(final Integer memberId, final MemberDTO memberDTO) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(memberDTO, member);
        memberRepository.save(member);
    }

    public void delete(final Integer memberId) {
        memberRepository.deleteById(memberId);
    }

    private MemberDTO mapToDTO(final Member member, final MemberDTO memberDTO) {
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setEmail(member.getEmail());
        memberDTO.setPassword(member.getPassword());
        memberDTO.setNickname(member.getNickname());
        memberDTO.setProfileImage(member.getProfileImage());
        memberDTO.setRewardPoint(member.getRewardPoint());
        memberDTO.setRole(member.getRole());
        memberDTO.setCreatedAt(member.getCreatedAt());
        memberDTO.setSocialType(member.getSocialType());
        memberDTO.setBirthDate(member.getBirthDate());
        memberDTO.setGender(member.getGender());
        memberDTO.setActivated(member.getActivated());
        memberDTO.setWinRate(member.getWinRate());
        return memberDTO;
    }

    private Member mapToEntity(final MemberDTO memberDTO, final Member member) {
        member.setEmail(memberDTO.getEmail());
        member.setPassword(memberDTO.getPassword());
        member.setNickname(memberDTO.getNickname());
        member.setProfileImage(memberDTO.getProfileImage());
        member.setRewardPoint(memberDTO.getRewardPoint());
        member.setRole(memberDTO.getRole());
        member.setCreatedAt(memberDTO.getCreatedAt());
        member.setSocialType(memberDTO.getSocialType());
        member.setBirthDate(memberDTO.getBirthDate());
        member.setGender(memberDTO.getGender());
        member.setActivated(memberDTO.getActivated());
        member.setWinRate(memberDTO.getWinRate());
        return member;
    }

    public boolean emailExists(final String email) {
        return memberRepository.existsByEmailIgnoreCase(email);
    }

    public ReferencedWarning getReferencedWarning(final Integer memberId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundException::new);
        final StudyMember memberStudyMember = studyMemberRepository.findFirstByMember(member);
        if (memberStudyMember != null) {
            referencedWarning.setKey("member.studyMember.member.referenced");
            referencedWarning.addParam(memberStudyMember.getStudyMemberId());
            return referencedWarning;
        }
        final AlarmRecipient memberAlarmRecipient = alarmRecipientRepository.findFirstByMember(member);
        if (memberAlarmRecipient != null) {
            referencedWarning.setKey("member.alarmRecipient.member.referenced");
            referencedWarning.addParam(memberAlarmRecipient.getAlarmRecipientId());
            return referencedWarning;
        }
        final StudyApplicant memberStudyApplicant = studyApplicantRepository.findFirstByMember(member);
        if (memberStudyApplicant != null) {
            referencedWarning.setKey("member.studyApplicant.member.referenced");
            referencedWarning.addParam(memberStudyApplicant.getApplicationId());
            return referencedWarning;
        }
        final OwnItem memberOwnItem = ownItemRepository.findFirstByMember(member);
        if (memberOwnItem != null) {
            referencedWarning.setKey("member.ownItem.member.referenced");
            referencedWarning.addParam(memberOwnItem.getOwnItemId());
            return referencedWarning;
        }
        return null;
    }

}
