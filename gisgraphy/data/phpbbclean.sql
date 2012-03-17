select pt.topic_title, pt.topic_id from phpbb_topics pt order by pt.topic_time desc limit 6;
select pp.topic_id,pp.post_subject from phpbb_posts pp order by pp.post_time desc limit 6;

select pu.username,pu.user_ip from  phpbb_users pu order by pu.user_regdate desc limit 20;

select phpbb_topics pt,phpbb_posts pp,phpbb_users pu,from phpbb_topics_posted ptp

#suppression des users ayant la meme ip
select  pu2.username,pu2.user_ip,pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#suppression des users;
delete  from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

topicid a supprimer pour tout les utilisateur ayant le meme ip que le topic donné :
select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id)) ;

##############################Posts

#quels posts vont etre supprimer (registered ip)
select post.post_subject from phpbb_posts post where post.topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id))) ;

#quels posts vont etre supprimer (post ip):
select post.post_subject from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#quels posts vont etre supprimer (post ip + registered ip):
select post.post_subject from phpbb_posts post where post.topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id))) union select post.post_subject from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#suppression des post des user ayant la meme ip que l id specifié


#suppression des posts (registered ip)
delete from phpbb_posts post where post.topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id))) ;

#suppression des posts (post ip)
delete from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#suppression des posts (post ip+registered ip)
delete  from phpbb_posts post where post.poster_ip in (select post.post_subject from phpbb_posts post where post.topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id))) union select post.post_subject from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id)) ;


##################################Topics

Quel topic vont etre supprimer 
select t1.topic_id  from phpbb_topics t1 where t1.topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id))) union select post.topic_id  from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#quel est l'ip de l'utilisateur :
select pu.user_ip,pu.username from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id;

#quels user vont etre supprimer
select  pu2.username,pu2.user_ip,pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id) ;

#suppression des topics (OK) :
delete from phpbb_topics  where topic_id in (select ptp2.topic_id from phpbb_topics_posted ptp2 where ptp2.user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id)) union select post.topic_id  from phpbb_posts post where post.poster_ip in ( select pu.user_ip from  phpbb_users pu, phpbb_topics_posted ptp where ptp.topic_id = 60 and ptp.user_id=pu.user_id)) ;


#suppression des topic posted en fonction du user_id (ok)
delete from phpbb_topics_posted  where user_id in (select pu2.user_id from phpbb_users pu2 where pu2.user_ip='194.8.75.132');



#suppression des posts (post ip)
delete  from phpbb_posts where poster_ip = '194.8.75.132';
delete  from phpbb_posts where poster_id in (select u.user_id from phpbb_users u where u.user_ip ='194.8.75.132');




#suppression des utilisateurs (OK)
delete from phpbb_users where user_ip ='194.8.75.132';

#last poster
select u.username from phpbb_users u where u.user_id in (select forum_last_poster_id from phpbb_forums where forum_id=3);

update phpbb_forums set forum_last_poster_id = 2;
update phpbb_forums set forum_last_poster_colour= 'AA0000';


update phpbb_forums set forum_last_poster_name = 'gisgraphy';

# sudo iptables -A INPUT -s 194.8.75.132 -j DROP 

