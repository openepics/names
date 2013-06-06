create view fnc_name(id, category, code, instance,  description, date_modified, modified_by) as
select concat('sys.', code) as id, 'System' as category, code, instance, description, date_modified, modified_by from fnc_system
union
select concat('subs.', code) as id,'Subsystem' as category, code, instance, description, date_modified, modified_by from subsystem
union 
select concat('dev.', code) as id,'Device Type' as category, code, instance, description, date_modified, modified_by from device_type
union
select concat('sigt.', code) as id,'Signal Type' as category, code, "", description, date_modified, modified_by from signal_type
union
select concat('sigd.', code) as id,'Signal Domain' as category, code, "", description, date_modified, modified_by from signal_domain
union
select concat('sigs.', code) as id,'Signal Suffix' as category, code, "", description, date_modified, modified_by from signal_suffix;
