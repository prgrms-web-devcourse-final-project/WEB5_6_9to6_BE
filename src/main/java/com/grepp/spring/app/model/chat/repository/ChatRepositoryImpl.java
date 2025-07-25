package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.QChat;
import com.grepp.spring.app.model.chat.entity.QChatRoom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom{


    private final JPAQueryFactory queryFactory;

    @Override
    public List<Chat> findAllRelevantChats(Long studyId, String username, Long memberId) {
        QChat chat = QChat.chat;
        QChatRoom chatRoom = QChatRoom.chatRoom;

        return queryFactory
            .selectFrom(chat)
            .join(chat.chatRoom, chatRoom)
            .where(
                chatRoom.study.studyId.eq(studyId),
                chat.receiverId.isNull()
                    .or(chat.receiverId.eq(username))
                    .or(chat.senderId.eq(memberId))
            )
            .orderBy(chat.createdAt.asc())
            .fetch();
    }

    @Override
    public List<Chat> findAllRelevantChatsPage(
        Long studyId,
        String username,
        Long memberId,
        LocalDateTime cursorCreatedAt,
        Long lastId,
        int pageSize) {
        QChat chat = QChat.chat;
        QChatRoom chatRoom = QChatRoom.chatRoom;

        BooleanBuilder condition = new BooleanBuilder()
            .and(chat.chatRoom.study.studyId.eq(studyId))
            .and(
                chat.receiverId.isNull()
                    .or(chat.receiverId.eq(username))
                    .or(chat.senderId.eq(memberId))
            );

        if (cursorCreatedAt != null && lastId != null) {
            condition.and(
                chat.createdAt.gt(cursorCreatedAt)
                    .or(chat.createdAt.eq(cursorCreatedAt)
                        .and(chat.id.gt(lastId)))
            );
        }

        return queryFactory
            .selectFrom(chat)
            .join(chat.chatRoom, chatRoom)
            .where(condition
            )
            .orderBy(chat.createdAt.asc(), chat.id.asc())
            .limit(pageSize)
            .fetch();
    }

}
