按月查看城市、商品类别的销售额
select to_char(data_yys.sell.buy_time , 'YYYY-MM'), data_yys.sell.sell_city,data_yys.goods.kind , sum(data_yys.sell.total_price) as total_price
from data_yys.sell left join data_yys.goods on data_yys.sell.goods_id = data_yys.goods.goods_id 
group by to_char(data_yys.sell.buy_time , 'YYYY-MM'), data_yys.sell.sell_city, data_yys.goods.kind
按月查看总体销售额、新增注册人数
select to_char(data_yys.sale.par_time, 'YYYY-MM') as year_month,sum(data_yys.sale.sale_price) as sum_sale from data_yys.sale group by to_char(data_yys.sale.par_time, 'YYYY-MM');
select to_char(data_yys.client.reg_time, 'YYYY-MM') as year_month,count(*) as client_count from data_yys.client group by to_char(data_yys.client.reg_time, 'YYYY-MM');
select data_yys.sale.sale_city as city,to_char(data_yys.sale.par_time, 'YYYY-MM') as year_month,sum(data_yys.sale.sale_price) as sum_sale from data_yys.sale group by to_char(data_yys.sale.par_time, 'YYYY-MM'),data_yys.sale.sale_city;
select to_char(data_yys.sale.par_time, 'YYYY-MM') as year_month,data_yys.sale.sale_city as city,data_yys.commodity.cate as com_cate,sum(data_yys.sale.sale_price) as sum_sale 
from data_yys.sale left join data_yys.commodity on data_yys.sale.com_code = data_yys.commodity.code
group by to_char(data_yys.sale.par_time, 'YYYY-MM'),data_yys.sale.sale_city,data_yys.commodity.cate;
select to_char(data_yys.sale.par_time, 'YYYY-MM') as year_month,data_yys.sale.sale_sex as sex,data_yys.commodity.cate as com_cate,sum(data_yys.sale.sale_price) as sum_sale 
from data_yys.sale left join data_yys.commodity on data_yys.sale.com_code = data_yys.commodity.code
group by to_char(data_yys.sale.par_time, 'YYYY-MM'),data_yys.sale.sale_sex,data_yys.commodity.cate;

INSERT INTO "data_yys"."client" VALUES ('101', 'aaa', '1841860154', 'uf0ZVjqq.com',10,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('102', 'aoa', '18415154', 'uf0ZVjqq@.com',9,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('103', 'kaa', '15244', 'uf0ZVjqq',8,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('104', 'ayfa', '1841864', 'uf0ZVjqq.@com',7,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('105', 'nba', '184154', 'uf0ZVjqq.com',6,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('106', 'aba', '18418d54', 'uf0ZVjqq.com',5,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('107', 'afa', '11860146764', 'uf0ZVjqq.com',4,0,'2020-07-05 12:43:01');
INSERT INTO "data_yys"."client" VALUES ('108', 'ahtkya', '115235y53', 'uf0ZVjqq.com',3,0,'2020-07-05 12:43:01');