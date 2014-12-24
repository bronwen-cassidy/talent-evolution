CREATE OR REPLACE VIEW subject_fileitem_autonomy_view AS
  SELECT
    si.id, c.first_name || ' ' || c.second_name AS title, si.node_id AS artefact_id,  si.owner_id,
      si.label AS content_title,
    NVL(si.scope,'RESTRICTED') as scope, 'S' as type, si.content_type_id,
    fi.blob_value as blob_value, si.file_extension, si.file_size
  FROM node_items si, node_item_blobs fi, subjects u, core_details c
  WHERE si.node_id = u.node_id
  AND u.cd_id=c.id
  AND si.id=fi.id
  AND si.status='LIVE'
WITH READ ONLY;

CREATE OR REPLACE VIEW pos_fileitem_autonomy_view AS
  SELECT
    pi.id, p.title, pi.node_id AS artefact_id, pi.owner_id,
     pi.label as content_title, NVL(pi.scope,'RESTRICTED') as scope,
    'P' as type, pi.content_type_id,
    pfi.blob_value as blob_value, pi.file_extension, pi.file_size
  FROM node_items pi, positions p, node_item_blobs pfi
  WHERE  pi.node_id = p.node_id
  AND pi.id=pfi.id
  AND pi.status='LIVE'
WITH READ ONLY;
commit;
