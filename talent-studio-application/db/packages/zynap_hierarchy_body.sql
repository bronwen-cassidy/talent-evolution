CREATE OR REPLACE
PACKAGE BODY zynap_hierarchy_sp AS



PROCEDURE insert_position_hi_children (
    id_ IN INTEGER
     )     
  is
  begin
  
   		INSERT INTO positions_hierarchy (id, root_id, hlevel)    		   		    
	 	 select  children.id, parents.root_id, parents.hlevel + children.hlevel
 		 from
 	        (select * from positions_hierarchy  where id = id_) parents, 	 
 	        (select * from positions_hierarchy  where root_id = id_) children ;

        INSERT INTO positions_hierarchy_inc (id, root_id, hlevel)
        	 	 select  children.id, parents.root_id, parents.hlevel + children.hlevel
         		 from
         	        (select * from positions_hierarchy  where id = id_) parents,
         	        (select * from positions_hierarchy  where root_id = id_) children ;


 end insert_position_hi_children;

 PROCEDURE insert_position_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 )
 IS

 BEGIN
    if parent_id_ is not null then
	  	INSERT INTO positions_hierarchy  (id, root_id, hlevel) VALUES (id_,parent_id_,1);

 		INSERT INTO positions_hierarchy (id, root_id, hlevel)
 		     select id_ as id , ph.root_id as root_id, hlevel + 1 from positions_hierarchy ph where ph.id = parent_id_;

        INSERT INTO positions_hierarchy_inc (id, root_id, hlevel)
             select id_ as id , ph.root_id as root_id, hlevel
             from positions_hierarchy ph where ph.id = id_ ;

 	end if;
    INSERT INTO positions_hierarchy_inc  (id, root_id, hlevel) VALUES (id_,id_,0);


 END insert_position_hi;


 PROCEDURE update_position_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 )
 is
 begin
    --Deleting the relationships betwen  my antecessors and succesors                                  
  delete_position_hi(id_);
  delete from positions_hierarchy where id = id_;
  delete from positions_hierarchy_inc where id = id_;
  insert_position_hi(id_,parent_id_);
  insert_position_hi_children(id_);
  
 end  update_position_hi;

 PROCEDURE delete_position_hi (
    id_ IN INTEGER
     )     
  is
  begin
	delete from positions_hierarchy  ph
	 where  exists
	( select 1 from POSITIONS_HIERARCHY parents,POSITIONS_HIERARCHY children
		where  parents.id = id_ and children.root_id = id_
       and ph.root_id = parents.root_id and ph.id= children.id) ;

	delete from positions_hierarchy_inc  ph
    	 where  exists
    	( select 1 from positions_hierarchy parents,positions_hierarchy children
    		where  parents.id = id_ and children.root_id = id_
           and ph.root_id = parents.root_id and ph.id= children.id) ;

    
 end delete_position_hi;              
 
 
 
 

PROCEDURE insert_OU_hi_children (
    id_ IN INTEGER
     )     
  is
  begin
  
   		INSERT INTO ou_hierarchy (id, root_id, hlevel)    		   		    
	 	 select  children.id, parents.root_id, parents.hlevel + children.hlevel
 		 from
 	        (select * from ou_hierarchy  where id = id_) parents, 	 
 	        (select * from ou_hierarchy  where root_id = id_) children ;

   		INSERT INTO ou_hierarchy_inc (id, root_id, hlevel)
	 	 select  children.id, parents.root_id, parents.hlevel + children.hlevel
 		 from
 	        (select * from ou_hierarchy  where id = id_) parents,
 	        (select * from ou_hierarchy  where root_id = id_) children ;

 end insert_OU_hi_children;    

 
  PROCEDURE insert_OU_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 )
 IS                                                                                                                                
 
 BEGIN                                    
    if parent_id_ is not null then
	  	INSERT INTO ou_hierarchy  (id, root_id, hlevel) VALUES (id_, parent_id_, 1);
 		INSERT INTO ou_hierarchy (id, root_id, hlevel) 
 		   select id_ as id , root_id as root_id, hlevel + 1 as hlevel from ou_hierarchy  where id = parent_id_;
 		INSERT INTO ou_hierarchy_inc (id, root_id, hlevel)
            select id_ as id , root_id as root_id, hlevel as hlevel from ou_hierarchy  where id = id_;

 	end if;
 	INSERT INTO ou_hierarchy_inc  (id, root_id, hlevel) VALUES (id_, id_, 0);
 
 END insert_ou_hi;   
 
 
 PROCEDURE update_ou_hi (
    id_ IN INTEGER,
    parent_id_ IN INTEGER
 )
 is
 begin
    --Deleting the relationships betwen  my antecessors and succesors                                  
   delete_ou_hi(id_);
   delete from ou_hierarchy where id = id_;
   delete from ou_hierarchy_inc where id = id_;    
   insert_ou_hi (id_,parent_id_);
   insert_OU_hi_children(id_);
    
  
 end  update_ou_hi;

 PROCEDURE delete_ou_hi (
    id_ IN INTEGER
     )     
  is
  begin
	delete from ou_hierarchy  ph
	 where  exists
	( select 1 from ou_hierarchy parents,ou_hierarchy children
		where  parents.id = id_ and children.root_id = id_
       and ph.root_id = parents.root_id and ph.id= children.id) ;

    delete from ou_hierarchy_inc  ph
	 where  exists
	( select 1 from ou_hierarchy parents,ou_hierarchy children
		where  parents.id = id_ and children.root_id = id_
       and ph.root_id = parents.root_id and ph.id= children.id) ;
    
 end delete_ou_hi;           
 
 FUNCTION isSubPrimaryAssociation
 (                               
 	qualifierId_ IN INTEGER
 ) RETURN boolean
 IS                    
  type_id_   varchar2(100);
 begin
  select type_id into type_id_ from lookup_values where id = qualifierId_;
  if type_id_ = 'POSITIONSUBJECTASSOC' then
  	return true;
  else 
    return false;
  end if;   	
     
 end isSubPrimaryAssociation;
 

END zynap_hierarchy_sp;
/
