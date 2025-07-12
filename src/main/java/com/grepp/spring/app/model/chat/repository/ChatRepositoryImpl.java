package com.grepp.spring.app.model.chat.repository;

import com.grepp.spring.app.model.chat.entity.Chat;
import com.grepp.spring.app.model.chat.entity.QChat;
import com.grepp.spring.app.model.chat.entity.QChatRoom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

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

}
