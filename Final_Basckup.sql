PGDMP     4                    {            LearningAutopilotDB    15.2    15.2 c               0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false                       0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false                       0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false                       1262    24826    LearningAutopilotDB    DATABASE     �   CREATE DATABASE "LearningAutopilotDB" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = icu LOCALE = 'en_US.UTF-8' ICU_LOCALE = 'en-US';
 %   DROP DATABASE "LearningAutopilotDB";
                postgres    false                        3079    25931    pg_trgm 	   EXTENSION     ;   CREATE EXTENSION IF NOT EXISTS pg_trgm WITH SCHEMA public;
    DROP EXTENSION pg_trgm;
                   false                       0    0    EXTENSION pg_trgm    COMMENT     e   COMMENT ON EXTENSION pg_trgm IS 'text similarity measurement and index searching based on trigrams';
                        false    2            &           1255    25397    Add_MaintenanceRecord()    FUNCTION       CREATE FUNCTION public."Add_MaintenanceRecord"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    record_type uuid;
BEGIN
    IF TG_OP = 'INSERT' THEN
        record_type = '48b6d732-9875-4235-b4a2-c248eaa1b678'; --ID of "Add" value in "RecordTypes"
	ELSIF TG_OP = 'UPDATE' AND NEW."Balance_Cost" = 0 AND OLD."Balance_Cost" <> 0 THEN
        record_type = '9519e68c-fa93-40b7-95ff-b359f25cc41b'; --ID of "Out of maintance" value in "RecordTypes"
    ELSIF TG_OP = 'UPDATE' THEN
        record_type = '443491bc-6102-4877-840a-e16a4a990491'; --ID of "Change" value in "RecordTypes"
    END IF;
    
    INSERT INTO "MaintenanceRecords" ("Equipment", "Record_Date", "Record_Type", "Description")
    VALUES (NEW."ID_Equipment", current_date, record_type, '');
    
    RETURN NEW;
END;
$$;
 0   DROP FUNCTION public."Add_MaintenanceRecord"();
       public          postgres    false            +           1255    25712    Calculate_Balance_Cost(uuid)    FUNCTION     2  CREATE FUNCTION public."Calculate_Balance_Cost"(e_id uuid) RETURNS bigint
    LANGUAGE plpgsql
    AS $$
DECLARE
	e_purchace_cost bigint;
	e_purchace_date date;
	e_end_of_life date;
BEGIN
	SELECT 
		"Purchase_Cost",
		"Purchase_Date",
		"End_Of_Life"
	INTO
		e_purchace_cost,
		e_purchace_date,
		e_end_of_life
	FROM "Equipment"
	WHERE "ID_Equipment" = e_id;
	
	IF (e_end_of_life <= current_date) THEN
		RETURN 0;
	ELSE
		RETURN 	e_purchace_cost * (e_end_of_life - current_date) / (e_end_of_life - e_purchace_date); --расчет balance_cost
	END IF;
END;
$$;
 :   DROP FUNCTION public."Calculate_Balance_Cost"(e_id uuid);
       public          postgres    false                       1255    25367    Classrooms_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."Classrooms_Delete"(IN c_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM "Classrooms"
    WHERE "ID_Classroom" = c_id;
END;
$$;
 9   DROP PROCEDURE public."Classrooms_Delete"(IN c_id uuid);
       public          postgres    false            %           1255    25366 4   Classrooms_UpdateOrInsert(uuid, integer, text, uuid) 	   PROCEDURE     Z  CREATE PROCEDURE public."Classrooms_UpdateOrInsert"(IN c_id uuid, IN c_office_number integer, IN c_location_address text, IN c_owner uuid)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF c_id = zero_uuid THEN
		INSERT INTO "Classrooms" ("Office_Number", "Location_Address", "Owner")
        VALUES (c_office_number, c_location_address, c_owner);
	ELSE
		UPDATE "Classrooms"
		SET "Office_Number" = c_office_number,
			"Location_Address" = c_location_address,
			"Owner" = c_owner
		WHERE "ID_Classroom" = c_id;
	END IF;
END;
$$;
 �   DROP PROCEDURE public."Classrooms_UpdateOrInsert"(IN c_id uuid, IN c_office_number integer, IN c_location_address text, IN c_owner uuid);
       public          postgres    false            ,           1255    25231     EquipmentCategories_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."EquipmentCategories_Delete"(IN category_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM "EquipmentCategories"
	WHERE "ID_Category" = category_id;
END
$$;
 I   DROP PROCEDURE public."EquipmentCategories_Delete"(IN category_id uuid);
       public          postgres    false                       1255    25244 4   EquipmentCategories_UpdateOrInsert(uuid, text, text) 	   PROCEDURE     W  CREATE PROCEDURE public."EquipmentCategories_UpdateOrInsert"(IN category_id uuid, IN category_name text, IN category_description text)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
    IF category_id = zero_uuid THEN
        INSERT INTO "EquipmentCategories" ("Category", "Description")
        VALUES (category_name, category_description);
    ELSE
        UPDATE "EquipmentCategories"
        SET "Category" = category_name, "Description" = category_description
        WHERE "ID_Category" = category_id;
    END IF;
END;
$$;
 �   DROP PROCEDURE public."EquipmentCategories_UpdateOrInsert"(IN category_id uuid, IN category_name text, IN category_description text);
       public          postgres    false                       1255    25293    Equipment_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."Equipment_Delete"(IN e_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM "Equipment"
    WHERE "ID_Equipment" = e_id;
END;
$$;
 8   DROP PROCEDURE public."Equipment_Delete"(IN e_id uuid);
       public          postgres    false            '           1255    25292 O   Equipment_UpdateOrInsert(uuid, integer, uuid, text, bigint, bigint, date, uuid) 	   PROCEDURE     �  CREATE PROCEDURE public."Equipment_UpdateOrInsert"(IN e_id uuid, IN e_invent_number integer, IN e_category uuid, IN e_model text, IN e_purchase_cost bigint, IN e_balance_cost bigint, IN e_end_of_life date, IN e_location uuid)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF e_id = zero_uuid THEN
		INSERT INTO "Equipment" ("Invent_Number", "Category", "Model", "Purchase_Cost",  "Balance_Cost", "End_Of_Life", "Location")
        VALUES (e_invent_number, e_category, e_model, e_purchase_cost, e_balance_cost, e_end_of_life, e_location);
	ELSE
		UPDATE "Equipment"
		SET "Invent_Number" = e_invent_number,
			"Category" = e_category,
			"Model" = e_model,
			"Purchase_Cost" = e_purchase_cost,
			"Balance_Cost" = e_balance_cost,
			"End_Of_Life" = e_end_of_life,
			"Location" = e_location
		WHERE "ID_Equipment" = e_id;
	END IF;
END;
$$;
 �   DROP PROCEDURE public."Equipment_UpdateOrInsert"(IN e_id uuid, IN e_invent_number integer, IN e_category uuid, IN e_model text, IN e_purchase_cost bigint, IN e_balance_cost bigint, IN e_end_of_life date, IN e_location uuid);
       public          postgres    false            (           1255    25708 U   Equipment_UpdateOrInsert(uuid, integer, uuid, text, date, bigint, bigint, date, uuid) 	   PROCEDURE     �  CREATE PROCEDURE public."Equipment_UpdateOrInsert"(IN e_id uuid, IN e_invent_number integer, IN e_category uuid, IN e_model text, IN e_purchase_date date, IN e_purchase_cost bigint, IN e_balance_cost bigint, IN e_end_of_life date, IN e_location uuid)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF e_id = zero_uuid THEN
		INSERT INTO "Equipment" ("Invent_Number", "Category", "Model", "Purchase_Date", "Purchase_Cost",  "Balance_Cost", "End_Of_Life", "Location")
        VALUES (e_invent_number, e_category, e_model, e_purchase_date, e_purchase_cost, e_balance_cost, e_end_of_life, e_location);
	ELSE
		UPDATE "Equipment"
		SET "Invent_Number" = e_invent_number,
			"Category" = e_category,
			"Model" = e_model,
			"Purchase_Date" = e_purchase_date,
			"Purchase_Cost" = e_purchase_cost,
			"Balance_Cost" = e_balance_cost,
			"End_Of_Life" = e_end_of_life,
			"Location" = e_location
		WHERE "ID_Equipment" = e_id;
	END IF;
END;
$$;
 �   DROP PROCEDURE public."Equipment_UpdateOrInsert"(IN e_id uuid, IN e_invent_number integer, IN e_category uuid, IN e_model text, IN e_purchase_date date, IN e_purchase_cost bigint, IN e_balance_cost bigint, IN e_end_of_life date, IN e_location uuid);
       public          postgres    false            /           1255    26018 $   Get_All_Add_MaintenanceRecords(text)    FUNCTION     �  CREATE FUNCTION public."Get_All_Add_MaintenanceRecords"(record_type text) RETURNS TABLE(id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
	RETURN QUERY
		SELECT
			"ID_Record"
		FROM "MaintenanceRecords" AS m
		JOIN "Equipment" AS e
			ON m."Equipment" = e."ID_Equipment"
		JOIN "RecordTypes" AS r
			ON m."Record_Type" = r."ID_Type"
		WHERE r."Record_Type" ~ record_type
		ORDER BY e."Invent_Number";
END;
$$;
 I   DROP FUNCTION public."Get_All_Add_MaintenanceRecords"(record_type text);
       public          postgres    false                       1255    25396    MaintenanceRecords_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."MaintenanceRecords_Delete"(IN r_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
    DELETE FROM "MaintenanceRecords"
    WHERE "ID_Record" = r_id;
END;
$$;
 A   DROP PROCEDURE public."MaintenanceRecords_Delete"(IN r_id uuid);
       public          postgres    false                       1255    25395 ?   MaintenanceRecords_UpdateOrInsert(uuid, uuid, date, uuid, text) 	   PROCEDURE     �  CREATE PROCEDURE public."MaintenanceRecords_UpdateOrInsert"(IN r_id uuid, IN r_equipment uuid, IN r_record_date date, IN r_record_type uuid, IN r_description text)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF r_id = zero_uuid THEN
		INSERT INTO "MaintenanceRecords" ("Equipment", "Record_Date", "Record_Type", "Description")
        VALUES (r_equipment, r_record_date, r_record_type, r_description);
	ELSE
		UPDATE "MaintenanceRecords"
		SET "ID_Record" = r_id,
			"Equipment" = r_equipment,
			"Record_Date" = r_record_date,
			"Record_Type" = r_record_type,
			"Description" = r_description
		WHERE "ID_Record" = r_id;
	END IF;
END;
$$;
 �   DROP PROCEDURE public."MaintenanceRecords_UpdateOrInsert"(IN r_id uuid, IN r_equipment uuid, IN r_record_date date, IN r_record_type uuid, IN r_description text);
       public          postgres    false            -           1255    25386    RecordTypes_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."RecordTypes_Delete"(IN r_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM "RecordTypes"
		WHERE "ID_Type" = r_id;
END
$$;
 :   DROP PROCEDURE public."RecordTypes_Delete"(IN r_id uuid);
       public          postgres    false            .           1255    25385 &   RecordTypes_UpdateOrInsert(uuid, text) 	   PROCEDURE     �  CREATE PROCEDURE public."RecordTypes_UpdateOrInsert"(IN r_id uuid, IN r_record_type text)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF r_id = zero_uuid THEN
		INSERT INTO "RecordTypes" ("Record_Type")
        VALUES (r_record_type);
	ELSE
		UPDATE "RecordTypes"
		SET "Record_Type" = r_record_type
		WHERE "ID_Type" = r_id;
	END IF;
END;
$$;
 Y   DROP PROCEDURE public."RecordTypes_UpdateOrInsert"(IN r_id uuid, IN r_record_type text);
       public          postgres    false                       1255    25535    Staff_Delete(uuid) 	   PROCEDURE     �   CREATE PROCEDURE public."Staff_Delete"(IN s_id uuid)
    LANGUAGE plpgsql
    AS $$
BEGIN
	DELETE FROM "Staff"
	WHERE "ID_Staff" = s_id;
END
$$;
 4   DROP PROCEDURE public."Staff_Delete"(IN s_id uuid);
       public          postgres    false                       1255    25534 &   Staff_UpdateOrInsert(uuid, text, text) 	   PROCEDURE     �  CREATE PROCEDURE public."Staff_UpdateOrInsert"(IN s_id uuid, IN s_fio text, IN s_phone text)
    LANGUAGE plpgsql
    AS $$
DECLARE 
	zero_uuid uuid;
BEGIN
	zero_uuid := '00000000-0000-0000-0000-000000000000';
	IF s_id = zero_uuid THEN
		INSERT INTO "Staff" ("FIO", "Phone")
        VALUES (s_fio, s_phone);
	ELSE
		UPDATE "Staff"
		SET "FIO" = s_fio,
			"Phone" = s_phone
		WHERE "ID_Staff" = s_id;
	END IF;
END;
$$;
 \   DROP PROCEDURE public."Staff_UpdateOrInsert"(IN s_id uuid, IN s_fio text, IN s_phone text);
       public          postgres    false            *           1255    25812    Update_3_b()    FUNCTION     �  CREATE FUNCTION public."Update_3_b"() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    UPDATE "Classrooms"
    SET
      "Office_Number" = NEW."Office_Number",
      "Location_Address" = NEW."Location_Address"
    WHERE "Classrooms"."Owner" = OLD."ID_Staff";
	
	UPDATE "Staff"
    SET
      "FIO" = NEW."FIO",
      "Phone" = NEW."Phone"
    WHERE "ID_Staff" = OLD."ID_Staff";
	
	RETURN NEW;
END;
$$;
 %   DROP FUNCTION public."Update_3_b"();
       public          postgres    false            )           1255    25717    Update_Balance_Cost() 	   PROCEDURE     O  CREATE PROCEDURE public."Update_Balance_Cost"()
    LANGUAGE plpgsql
    AS $$
DECLARE
	new_balance_cost bigint;
	"Balance_Cost_Cursor" CURSOR FOR
		SELECT 
			"ID_Equipment",
			"Balance_Cost"
		FROM "Equipment";
BEGIN
	FOR record IN "Balance_Cost_Cursor" LOOP
		new_balance_cost := (SELECT "Calculate_Balance_Cost"(record."ID_Equipment"));
		UPDATE "Equipment"
			SET "Balance_Cost" = new_balance_cost
			WHERE "ID_Equipment" = record."ID_Equipment";
			
 		IF (record."Balance_Cost" <> 0 AND new_balance_cost >= 0) THEN
 			COMMIT;
 		ELSE
 			ROLLBACK;
 		END IF;
		
	END LOOP;
END;
$$;
 /   DROP PROCEDURE public."Update_Balance_Cost"();
       public          postgres    false            �            1259    25245 	   Equipment    TABLE     j  CREATE TABLE public."Equipment" (
    "ID_Equipment" uuid DEFAULT gen_random_uuid() NOT NULL,
    "Invent_Number" integer NOT NULL,
    "Category" uuid NOT NULL,
    "Model" text NOT NULL,
    "Purchase_Cost" bigint NOT NULL,
    "Balance_Cost" bigint NOT NULL,
    "End_Of_Life" date NOT NULL,
    "Location" uuid NOT NULL,
    "Purchase_Date" date NOT NULL
);
    DROP TABLE public."Equipment";
       public         heap    postgres    false                       0    0    TABLE "Equipment"    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."Equipment" TO "Equipment_Execute";
GRANT SELECT ON TABLE public."Equipment" TO "MaintenanceRecords_Execute";
          public          postgres    false    218            �            1259    25141    EquipmentCategories    TABLE     �   CREATE TABLE public."EquipmentCategories" (
    "ID_Category" uuid DEFAULT gen_random_uuid() NOT NULL,
    "Category" text NOT NULL,
    "Description" text
);
 )   DROP TABLE public."EquipmentCategories";
       public         heap    postgres    false                       0    0    TABLE "EquipmentCategories"    ACL     �   GRANT SELECT ON TABLE public."EquipmentCategories" TO "Equipment_Execute";
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."EquipmentCategories" TO "EquipmentCategories_Execute";
          public          postgres    false    215            �            1259    25718    3_a    VIEW     4  CREATE VIEW public."3_a" AS
 SELECT e."Invent_Number",
    ec."Category",
    e."Model",
    e."End_Of_Life",
        CASE
            WHEN ((e."End_Of_Life" - CURRENT_DATE) <= 0) THEN 'Срок жизни закончился'::text
            WHEN ((e."End_Of_Life" - CURRENT_DATE) < 90) THEN 'Срок жизни подходит к концу'::text
            ELSE 'Срок жизни в норме'::text
        END AS "End_Of_Life_Message"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")));
    DROP VIEW public."3_a";
       public          postgres    false    218    218    218    215    215    218            �            1259    25171 
   Classrooms    TABLE     �   CREATE TABLE public."Classrooms" (
    "ID_Classroom" uuid DEFAULT gen_random_uuid() NOT NULL,
    "Office_Number" integer NOT NULL,
    "Location_Address" text NOT NULL,
    "Owner" uuid NOT NULL
);
     DROP TABLE public."Classrooms";
       public         heap    postgres    false                       0    0    TABLE "Classrooms"    ACL     �   GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."Classrooms" TO "Classrooms_Execute";
GRANT SELECT ON TABLE public."Classrooms" TO "Equipment_Execute";
          public          postgres    false    217            �            1259    25161    Staff    TABLE     �   CREATE TABLE public."Staff" (
    "ID_Staff" uuid DEFAULT gen_random_uuid() NOT NULL,
    "FIO" text NOT NULL,
    "Phone" text NOT NULL
);
    DROP TABLE public."Staff";
       public         heap    postgres    false                       0    0    TABLE "Staff"    ACL     �   GRANT SELECT ON TABLE public."Staff" TO "Classrooms_Execute";
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."Staff" TO "Staff_Execute";
          public          postgres    false    216            �            1259    25814    3_b    VIEW     �   CREATE VIEW public."3_b" AS
 SELECT c."Office_Number",
    c."Location_Address",
    s."ID_Staff",
    s."FIO",
    s."Phone"
   FROM (public."Classrooms" c
     JOIN public."Staff" s ON ((c."Owner" = s."ID_Staff")));
    DROP VIEW public."3_b";
       public          postgres    false    217    216    216    216    217    217            �            1259    25552 	   3_d_first    VIEW     �   CREATE VIEW public."3_d_first" AS
 SELECT ec."Category",
    ( SELECT sum(e."Purchase_Cost") AS sum
           FROM public."Equipment" e
          WHERE (e."Category" = ec."ID_Category")) AS "Total_Purchase_Cost"
   FROM public."EquipmentCategories" ec;
    DROP VIEW public."3_d_first";
       public          postgres    false    215    215    218    218            �            1259    25320    MaintenanceRecords    TABLE     �   CREATE TABLE public."MaintenanceRecords" (
    "ID_Record" uuid DEFAULT gen_random_uuid() NOT NULL,
    "Equipment" uuid NOT NULL,
    "Record_Date" date NOT NULL,
    "Record_Type" uuid NOT NULL,
    "Description" text
);
 (   DROP TABLE public."MaintenanceRecords";
       public         heap    postgres    false                       0    0    TABLE "MaintenanceRecords"    ACL     �   GRANT INSERT ON TABLE public."MaintenanceRecords" TO "Equipment_Execute";
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."MaintenanceRecords" TO "MaintenanceRecords_Execute";
          public          postgres    false    221            �            1259    25310    RecordTypes    TABLE     ~   CREATE TABLE public."RecordTypes" (
    "ID_Type" uuid DEFAULT gen_random_uuid() NOT NULL,
    "Record_Type" text NOT NULL
);
 !   DROP TABLE public."RecordTypes";
       public         heap    postgres    false                       0    0    TABLE "RecordTypes"    ACL     �   GRANT SELECT ON TABLE public."RecordTypes" TO "MaintenanceRecords_Execute";
GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public."RecordTypes" TO "RecordTypes_Execute";
          public          postgres    false    220            �            1259    25556 
   3_d_second    VIEW     �  CREATE VIEW public."3_d_second" AS
 SELECT ec."Category",
    e."Model",
    m."Record_Date" AS "Latest_Maintenance_Date",
    r."Record_Type" AS "Latest_Maintenance_Type",
    m."Description" AS "Latest_Maintenance_Description"
   FROM (((public."Equipment" e
     JOIN public."MaintenanceRecords" m ON ((e."ID_Equipment" = m."Equipment")))
     JOIN public."RecordTypes" r ON ((r."ID_Type" = m."Record_Type")))
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")))
  WHERE (m."Record_Date" = ( SELECT max("MaintenanceRecords"."Record_Date") AS max
           FROM public."MaintenanceRecords"
          WHERE ("MaintenanceRecords"."Equipment" = e."ID_Equipment")));
    DROP VIEW public."3_d_second";
       public          postgres    false    215    215    218    218    218    220    220    221    221    221    221            �            1259    25561 	   3_d_third    VIEW     �  CREATE VIEW public."3_d_third" AS
 SELECT ( SELECT s."FIO"
           FROM (public."Staff" s
             JOIN public."Classrooms" c ON ((s."ID_Staff" = c."Owner")))
          WHERE (e."Location" = c."ID_Classroom")) AS "FIO",
    e."Invent_Number",
    ec."Category",
    e."Model"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")));
    DROP VIEW public."3_d_third";
       public          postgres    false    218    218    218    218    217    217    216    216    215    215            �            1259    25566    3_e    VIEW     [  CREATE VIEW public."3_e" AS
 SELECT c."Office_Number",
    ec."Category",
    count(*) AS "Total_Equipment_Max"
   FROM ((public."Classrooms" c
     JOIN public."Equipment" e ON ((c."ID_Classroom" = e."Location")))
     JOIN public."EquipmentCategories" ec ON ((e."Category" = ec."ID_Category")))
  GROUP BY c."ID_Classroom", c."Office_Number", ec."Category"
 HAVING (count(*) = ( SELECT max("Total_Equipment_In_Each_Classroom"."Total_Equipment") AS max
           FROM ( SELECT c_1."ID_Classroom",
                    c_1."Office_Number",
                    ec_1."Category",
                    count(*) AS "Total_Equipment"
                   FROM ((public."Classrooms" c_1
                     JOIN public."Equipment" e_1 ON ((c_1."ID_Classroom" = e_1."Location")))
                     JOIN public."EquipmentCategories" ec_1 ON ((e_1."Category" = ec_1."ID_Category")))
                  GROUP BY c_1."ID_Classroom", c_1."Office_Number", ec_1."Category") "Total_Equipment_In_Each_Classroom"
          WHERE ("Total_Equipment_In_Each_Classroom"."ID_Classroom" = c."ID_Classroom")))
  ORDER BY c."Office_Number";
    DROP VIEW public."3_e";
       public          postgres    false    217    218    218    217    215    215            �            1259    25579    3_f_all    VIEW     J  CREATE VIEW public."3_f_all" AS
 SELECT e."Invent_Number",
    ec."Category",
    e."Model",
    e."End_Of_Life"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")))
  WHERE (e."End_Of_Life" >= ALL ( SELECT "Equipment"."End_Of_Life"
           FROM public."Equipment"));
    DROP VIEW public."3_f_all";
       public          postgres    false    215    218    218    218    218    215            �            1259    25583    3_f_any    VIEW     Z  CREATE VIEW public."3_f_any" AS
 SELECT e."Invent_Number",
    ec."Category",
    e."Model",
    e."End_Of_Life"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")))
  WHERE ((ec."Category" = 'Мебель'::text) AND (e."Model" ~~ ANY (ARRAY['%Стол%'::text, '%Стул%'::text])));
    DROP VIEW public."3_f_any";
       public          postgres    false    218    218    218    215    215    218            �            1259    25484 	   3_с_from    VIEW     �  CREATE VIEW public."3_с_from" AS
 SELECT "Staff"."FIO",
    "Equipment_In_Classroom"."Equipment_Count"
   FROM ( SELECT c."Owner",
            count(e."ID_Equipment") AS "Equipment_Count"
           FROM (public."Classrooms" c
             LEFT JOIN public."Equipment" e ON ((c."ID_Classroom" = e."Location")))
          GROUP BY c."Owner") "Equipment_In_Classroom",
    public."Staff"
  WHERE ("Equipment_In_Classroom"."Owner" = "Staff"."ID_Staff");
    DROP VIEW public."3_с_from";
       public          postgres    false    216    217    217    218    218    216            �            1259    25479    3_с_select    VIEW     �  CREATE VIEW public."3_с_select" AS
 SELECT e."Invent_Number",
    ec."Category",
    e."Model",
    ( SELECT count(*) AS count
           FROM public."MaintenanceRecords"
          WHERE ("MaintenanceRecords"."Equipment" = e."ID_Equipment")) AS "Maintenance_Record_Count"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")));
     DROP VIEW public."3_с_select";
       public          postgres    false    218    215    215    221    218    218    218            �            1259    25489 
   3_с_where    VIEW     �  CREATE VIEW public."3_с_where" AS
 SELECT e."Invent_Number",
    ec."Category",
    e."Model"
   FROM (public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((ec."ID_Category" = e."Category")))
  WHERE (e."ID_Equipment" IN ( SELECT m."Equipment"
           FROM (public."MaintenanceRecords" m
             JOIN public."RecordTypes" r ON ((r."ID_Type" = m."Record_Type")))
          WHERE (r."Record_Type" = 'Out of maintenance'::text)));
    DROP VIEW public."3_с_where";
       public          postgres    false    218    221    221    220    220    218    218    218    215    215            �            1259    25440    Classrooms_ClientView    VIEW     	  CREATE VIEW public."Classrooms_ClientView" AS
 SELECT c."ID_Classroom",
    c."Office_Number",
    c."Location_Address",
    s."FIO" AS "Owner"
   FROM (public."Classrooms" c
     JOIN public."Staff" s ON ((c."Owner" = s."ID_Staff")))
  ORDER BY c."Office_Number";
 *   DROP VIEW public."Classrooms_ClientView";
       public          postgres    false    217    217    217    216    216    217                       0    0    TABLE "Classrooms_ClientView"    ACL     K   GRANT SELECT ON TABLE public."Classrooms_ClientView" TO "Classrooms_Read";
          public          postgres    false    224            �            1259    25444    Classrooms_RawView    VIEW     �   CREATE VIEW public."Classrooms_RawView" AS
 SELECT "Classrooms"."ID_Classroom",
    "Classrooms"."Office_Number",
    "Classrooms"."Location_Address",
    "Classrooms"."Owner"
   FROM public."Classrooms";
 '   DROP VIEW public."Classrooms_RawView";
       public          postgres    false    217    217    217    217                       0    0    TABLE "Classrooms_RawView"    ACL     K   GRANT SELECT ON TABLE public."Classrooms_RawView" TO "Classrooms_Execute";
          public          postgres    false    225            �            1259    25465    EquipmentCategories_ClientView    VIEW       CREATE VIEW public."EquipmentCategories_ClientView" AS
 SELECT "EquipmentCategories"."ID_Category",
    "EquipmentCategories"."Category",
    "EquipmentCategories"."Description"
   FROM public."EquipmentCategories"
  ORDER BY "EquipmentCategories"."Category";
 3   DROP VIEW public."EquipmentCategories_ClientView";
       public          postgres    false    215    215    215                       0    0 &   TABLE "EquipmentCategories_ClientView"    ACL     ]   GRANT SELECT ON TABLE public."EquipmentCategories_ClientView" TO "EquipmentCategories_Read";
          public          postgres    false    227            �            1259    25306    EquipmentCategories_RawView    VIEW     �   CREATE VIEW public."EquipmentCategories_RawView" AS
 SELECT "EquipmentCategories"."ID_Category",
    "EquipmentCategories"."Category",
    "EquipmentCategories"."Description"
   FROM public."EquipmentCategories";
 0   DROP VIEW public."EquipmentCategories_RawView";
       public          postgres    false    215    215    215                       0    0 #   TABLE "EquipmentCategories_RawView"    ACL     ]   GRANT SELECT ON TABLE public."EquipmentCategories_RawView" TO "EquipmentCategories_Execute";
          public          postgres    false    219            �            1259    25699    Equipment_ClientView    VIEW     �  CREATE VIEW public."Equipment_ClientView" AS
 SELECT e."ID_Equipment",
    e."Invent_Number",
    ec."Category",
    e."Model",
    e."Purchase_Date",
    e."Purchase_Cost",
    e."Balance_Cost",
    e."End_Of_Life",
    c."Office_Number" AS "Location"
   FROM ((public."Equipment" e
     JOIN public."EquipmentCategories" ec ON ((e."Category" = ec."ID_Category")))
     JOIN public."Classrooms" c ON ((e."Location" = c."ID_Classroom")))
  ORDER BY e."Invent_Number";
 )   DROP VIEW public."Equipment_ClientView";
       public          postgres    false    217    218    218    218    218    218    218    218    218    217    215    215    218                       0    0    TABLE "Equipment_ClientView"    ACL     I   GRANT SELECT ON TABLE public."Equipment_ClientView" TO "Equipment_Read";
          public          postgres    false    239            �            1259    25704    Equipment_RawView    VIEW     \  CREATE VIEW public."Equipment_RawView" AS
 SELECT "Equipment"."ID_Equipment",
    "Equipment"."Invent_Number",
    "Equipment"."Category",
    "Equipment"."Model",
    "Equipment"."Purchase_Date",
    "Equipment"."Purchase_Cost",
    "Equipment"."Balance_Cost",
    "Equipment"."End_Of_Life",
    "Equipment"."Location"
   FROM public."Equipment";
 &   DROP VIEW public."Equipment_RawView";
       public          postgres    false    218    218    218    218    218    218    218    218    218                       0    0    TABLE "Equipment_RawView"    ACL     I   GRANT SELECT ON TABLE public."Equipment_RawView" TO "Equipment_Execute";
          public          postgres    false    240            �            1259    25777    MaintenanceRecords_ClientView    VIEW     �  CREATE VIEW public."MaintenanceRecords_ClientView" AS
 SELECT m."ID_Record",
    e."Invent_Number" AS "Equipment",
    m."Record_Date",
    r."Record_Type",
    m."Description"
   FROM ((public."MaintenanceRecords" m
     JOIN public."Equipment" e ON ((m."Equipment" = e."ID_Equipment")))
     JOIN public."RecordTypes" r ON ((m."Record_Type" = r."ID_Type")))
  ORDER BY m."Record_Date" DESC;
 2   DROP VIEW public."MaintenanceRecords_ClientView";
       public          postgres    false    221    221    218    220    220    218    221    221    221                       0    0 %   TABLE "MaintenanceRecords_ClientView"    ACL     [   GRANT SELECT ON TABLE public."MaintenanceRecords_ClientView" TO "MaintenanceRecords_Read";
          public          postgres    false    242            �            1259    25391    MaintenanceRecords_RawView    VIEW       CREATE VIEW public."MaintenanceRecords_RawView" AS
 SELECT "MaintenanceRecords"."ID_Record",
    "MaintenanceRecords"."Equipment",
    "MaintenanceRecords"."Record_Date",
    "MaintenanceRecords"."Record_Type",
    "MaintenanceRecords"."Description"
   FROM public."MaintenanceRecords";
 /   DROP VIEW public."MaintenanceRecords_RawView";
       public          postgres    false    221    221    221    221    221                       0    0 "   TABLE "MaintenanceRecords_RawView"    ACL     [   GRANT SELECT ON TABLE public."MaintenanceRecords_RawView" TO "MaintenanceRecords_Execute";
          public          postgres    false    223            �            1259    25452    RecordTypes_ClientView    VIEW     �   CREATE VIEW public."RecordTypes_ClientView" AS
 SELECT "RecordTypes"."ID_Type",
    "RecordTypes"."Record_Type"
   FROM public."RecordTypes"
  ORDER BY "RecordTypes"."Record_Type";
 +   DROP VIEW public."RecordTypes_ClientView";
       public          postgres    false    220    220                       0    0    TABLE "RecordTypes_ClientView"    ACL     M   GRANT SELECT ON TABLE public."RecordTypes_ClientView" TO "RecordTypes_Read";
          public          postgres    false    226            �            1259    25381    RecordTypes_RawView    VIEW     �   CREATE VIEW public."RecordTypes_RawView" AS
 SELECT "RecordTypes"."ID_Type",
    "RecordTypes"."Record_Type"
   FROM public."RecordTypes";
 (   DROP VIEW public."RecordTypes_RawView";
       public          postgres    false    220    220                       0    0    TABLE "RecordTypes_RawView"    ACL     M   GRANT SELECT ON TABLE public."RecordTypes_RawView" TO "RecordTypes_Execute";
          public          postgres    false    222            �            1259    25526    Staff_ClientView    VIEW     �   CREATE VIEW public."Staff_ClientView" AS
 SELECT "Staff"."ID_Staff",
    "Staff"."FIO",
    "Staff"."Phone"
   FROM public."Staff"
  ORDER BY "Staff"."FIO";
 %   DROP VIEW public."Staff_ClientView";
       public          postgres    false    216    216    216                        0    0    TABLE "Staff_ClientView"    ACL     A   GRANT SELECT ON TABLE public."Staff_ClientView" TO "Staff_Read";
          public          postgres    false    231            �            1259    25530    Staff_RawView    VIEW     �   CREATE VIEW public."Staff_RawView" AS
 SELECT "Staff"."ID_Staff",
    "Staff"."FIO",
    "Staff"."Phone"
   FROM public."Staff";
 "   DROP VIEW public."Staff_RawView";
       public          postgres    false    216    216    216            !           0    0    TABLE "Staff_RawView"    ACL     A   GRANT SELECT ON TABLE public."Staff_RawView" TO "Staff_Execute";
          public          postgres    false    232                      0    25171 
   Classrooms 
   TABLE DATA           d   COPY public."Classrooms" ("ID_Classroom", "Office_Number", "Location_Address", "Owner") FROM stdin;
    public          postgres    false    217   �                 0    25245 	   Equipment 
   TABLE DATA           �   COPY public."Equipment" ("ID_Equipment", "Invent_Number", "Category", "Model", "Purchase_Cost", "Balance_Cost", "End_Of_Life", "Location", "Purchase_Date") FROM stdin;
    public          postgres    false    218   )�                 0    25141    EquipmentCategories 
   TABLE DATA           Y   COPY public."EquipmentCategories" ("ID_Category", "Category", "Description") FROM stdin;
    public          postgres    false    215   ^�                 0    25320    MaintenanceRecords 
   TABLE DATA           u   COPY public."MaintenanceRecords" ("ID_Record", "Equipment", "Record_Date", "Record_Type", "Description") FROM stdin;
    public          postgres    false    221   ^�                 0    25310    RecordTypes 
   TABLE DATA           A   COPY public."RecordTypes" ("ID_Type", "Record_Type") FROM stdin;
    public          postgres    false    220   ��                 0    25161    Staff 
   TABLE DATA           =   COPY public."Staff" ("ID_Staff", "FIO", "Phone") FROM stdin;
    public          postgres    false    216   �       G           2606    25180 '   Classrooms Classrooms_Office_Number_key 
   CONSTRAINT     q   ALTER TABLE ONLY public."Classrooms"
    ADD CONSTRAINT "Classrooms_Office_Number_key" UNIQUE ("Office_Number");
 U   ALTER TABLE ONLY public."Classrooms" DROP CONSTRAINT "Classrooms_Office_Number_key";
       public            postgres    false    217            I           2606    25178    Classrooms Classrooms_pk 
   CONSTRAINT     f   ALTER TABLE ONLY public."Classrooms"
    ADD CONSTRAINT "Classrooms_pk" PRIMARY KEY ("ID_Classroom");
 F   ALTER TABLE ONLY public."Classrooms" DROP CONSTRAINT "Classrooms_pk";
       public            postgres    false    217            ?           2606    25150 4   EquipmentCategories EquipmentCategories_Category_key 
   CONSTRAINT     y   ALTER TABLE ONLY public."EquipmentCategories"
    ADD CONSTRAINT "EquipmentCategories_Category_key" UNIQUE ("Category");
 b   ALTER TABLE ONLY public."EquipmentCategories" DROP CONSTRAINT "EquipmentCategories_Category_key";
       public            postgres    false    215            A           2606    25148 *   EquipmentCategories EquipmentCategories_pk 
   CONSTRAINT     w   ALTER TABLE ONLY public."EquipmentCategories"
    ADD CONSTRAINT "EquipmentCategories_pk" PRIMARY KEY ("ID_Category");
 X   ALTER TABLE ONLY public."EquipmentCategories" DROP CONSTRAINT "EquipmentCategories_pk";
       public            postgres    false    215            K           2606    25254 %   Equipment Equipment_Invent_Number_key 
   CONSTRAINT     o   ALTER TABLE ONLY public."Equipment"
    ADD CONSTRAINT "Equipment_Invent_Number_key" UNIQUE ("Invent_Number");
 S   ALTER TABLE ONLY public."Equipment" DROP CONSTRAINT "Equipment_Invent_Number_key";
       public            postgres    false    218            M           2606    25252    Equipment Equipment_pk 
   CONSTRAINT     d   ALTER TABLE ONLY public."Equipment"
    ADD CONSTRAINT "Equipment_pk" PRIMARY KEY ("ID_Equipment");
 D   ALTER TABLE ONLY public."Equipment" DROP CONSTRAINT "Equipment_pk";
       public            postgres    false    218            U           2606    25327 (   MaintenanceRecords MaintenanceRecords_pk 
   CONSTRAINT     s   ALTER TABLE ONLY public."MaintenanceRecords"
    ADD CONSTRAINT "MaintenanceRecords_pk" PRIMARY KEY ("ID_Record");
 V   ALTER TABLE ONLY public."MaintenanceRecords" DROP CONSTRAINT "MaintenanceRecords_pk";
       public            postgres    false    221            P           2606    25319 '   RecordTypes RecordTypes_Record_type_key 
   CONSTRAINT     o   ALTER TABLE ONLY public."RecordTypes"
    ADD CONSTRAINT "RecordTypes_Record_type_key" UNIQUE ("Record_Type");
 U   ALTER TABLE ONLY public."RecordTypes" DROP CONSTRAINT "RecordTypes_Record_type_key";
       public            postgres    false    220            R           2606    25317    RecordTypes RecordTypes_pk 
   CONSTRAINT     c   ALTER TABLE ONLY public."RecordTypes"
    ADD CONSTRAINT "RecordTypes_pk" PRIMARY KEY ("ID_Type");
 H   ALTER TABLE ONLY public."RecordTypes" DROP CONSTRAINT "RecordTypes_pk";
       public            postgres    false    220            C           2606    25518    Staff Staff_Phone_key 
   CONSTRAINT     W   ALTER TABLE ONLY public."Staff"
    ADD CONSTRAINT "Staff_Phone_key" UNIQUE ("Phone");
 C   ALTER TABLE ONLY public."Staff" DROP CONSTRAINT "Staff_Phone_key";
       public            postgres    false    216            E           2606    25168    Staff Staff_pk 
   CONSTRAINT     X   ALTER TABLE ONLY public."Staff"
    ADD CONSTRAINT "Staff_pk" PRIMARY KEY ("ID_Staff");
 <   ALTER TABLE ONLY public."Staff" DROP CONSTRAINT "Staff_pk";
       public            postgres    false    216            =           1259    26012    Description_GIN_Index    INDEX     t   CREATE INDEX "Description_GIN_Index" ON public."EquipmentCategories" USING gin ("Description" public.gin_trgm_ops);
 +   DROP INDEX public."Description_GIN_Index";
       public            postgres    false    2    2    2    2    2    2    2    2    2    2    2    2    215            S           1259    25822    ID_Record_Index    INDEX     X   CREATE INDEX "ID_Record_Index" ON public."MaintenanceRecords" USING hash ("ID_Record");
 %   DROP INDEX public."ID_Record_Index";
       public            postgres    false    221            N           1259    26013    Invent_Number_BTREE_Index    INDEX     ^   CREATE INDEX "Invent_Number_BTREE_Index" ON public."Equipment" USING btree ("Invent_Number");
 /   DROP INDEX public."Invent_Number_BTREE_Index";
       public            postgres    false    218            V           1259    25930    Record_Date_BRIN_Index    INDEX     a   CREATE INDEX "Record_Date_BRIN_Index" ON public."MaintenanceRecords" USING brin ("Record_Date");
 ,   DROP INDEX public."Record_Date_BRIN_Index";
       public            postgres    false    221            \           2620    25404    Equipment Perform_Maintenance    TRIGGER     �   CREATE TRIGGER "Perform_Maintenance" AFTER INSERT OR UPDATE ON public."Equipment" FOR EACH ROW EXECUTE FUNCTION public."Add_MaintenanceRecord"();
 :   DROP TRIGGER "Perform_Maintenance" ON public."Equipment";
       public          postgres    false    218    294            ]           2620    25818    3_b Update_Trigger_For_3_b    TRIGGER        CREATE TRIGGER "Update_Trigger_For_3_b" INSTEAD OF UPDATE ON public."3_b" FOR EACH ROW EXECUTE FUNCTION public."Update_3_b"();
 7   DROP TRIGGER "Update_Trigger_For_3_b" ON public."3_b";
       public          postgres    false    298    243            W           2606    25420    Classrooms Classrooms_fk0    FK CONSTRAINT     �   ALTER TABLE ONLY public."Classrooms"
    ADD CONSTRAINT "Classrooms_fk0" FOREIGN KEY ("Owner") REFERENCES public."Staff"("ID_Staff") ON DELETE RESTRICT;
 G   ALTER TABLE ONLY public."Classrooms" DROP CONSTRAINT "Classrooms_fk0";
       public          postgres    false    217    3653    216            X           2606    25410    Equipment Equipment_fk0    FK CONSTRAINT     �   ALTER TABLE ONLY public."Equipment"
    ADD CONSTRAINT "Equipment_fk0" FOREIGN KEY ("Category") REFERENCES public."EquipmentCategories"("ID_Category") ON DELETE RESTRICT;
 E   ALTER TABLE ONLY public."Equipment" DROP CONSTRAINT "Equipment_fk0";
       public          postgres    false    218    3649    215            Y           2606    25415    Equipment Equipment_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public."Equipment"
    ADD CONSTRAINT "Equipment_fk1" FOREIGN KEY ("Location") REFERENCES public."Classrooms"("ID_Classroom") ON DELETE RESTRICT;
 E   ALTER TABLE ONLY public."Equipment" DROP CONSTRAINT "Equipment_fk1";
       public          postgres    false    218    217    3657            Z           2606    25399 )   MaintenanceRecords MaintenanceRecords_fk0    FK CONSTRAINT     �   ALTER TABLE ONLY public."MaintenanceRecords"
    ADD CONSTRAINT "MaintenanceRecords_fk0" FOREIGN KEY ("Equipment") REFERENCES public."Equipment"("ID_Equipment") ON DELETE CASCADE;
 W   ALTER TABLE ONLY public."MaintenanceRecords" DROP CONSTRAINT "MaintenanceRecords_fk0";
       public          postgres    false    221    218    3661            [           2606    25405 )   MaintenanceRecords MaintenanceRecords_fk1    FK CONSTRAINT     �   ALTER TABLE ONLY public."MaintenanceRecords"
    ADD CONSTRAINT "MaintenanceRecords_fk1" FOREIGN KEY ("Record_Type") REFERENCES public."RecordTypes"("ID_Type") ON DELETE RESTRICT;
 W   ALTER TABLE ONLY public."MaintenanceRecords" DROP CONSTRAINT "MaintenanceRecords_fk1";
       public          postgres    false    220    221    3666               2  x����q1E��*^��@��	�O�Rg��h�Rⱞ[�vd� ��8݅mq��V�������bC�Q���z���}�����s�>�0e��R�\(�����T����N�d�$M`�Z���,�6"�q����\4z�$!��ڄ$�Lƪ|���T(����cQ�U��X��H�_M4�ˌ�SC��K����sS:rJ��}==��������߮�P���}�59
z��tE'�AK���x$�:R>�z�'�,�$��k���PX:��������y|��J`��e�Q)�I���<� �Y�         %  x�����$U��꧘0���mBLJr�bH�BFB�H7#D���5o���g �խ��Oٟ�vM��3���j��&��̶Z�Ŵ1��z�Td���@8��\G_<2���zx���������_?|�������=|�7�[�#܆�Ѐm�͵.%X�(�5k�/^�WV���N0�Y�&DМ���)�a���+7>�������A�D��ǐL$�*
�n�k�:�q����d������EZ?�=� �i� k^A)I�z�̓�Z��ɛ\��3�5y���:10�S���,ۋy���|���g�Xn�c�x�d���:�=dj�9�s`�&�����\��l��QS�(�W4|�P��@[��B��p3�*�0�U��F�4#�I<����W0�#Y�hӭn|�[��6��k��m�j�
ct��eW7ʍn�������Wa�_c�^>��nӃ���p�XQn4�����J	�5�P��-6:ZE���9�y��f��%dPR�"	�j���Դ}��g�λ��������S|Cf��|ӌ(�,dZ���Q�\�2-DǐK��%m%��0/��U��%qq�+"�_Ds�"��y,a���4���В�f52�z��/b>��5�� ��X�4I!��Ҙ9�������÷��q�����;��-t,�-_��7/ *~���$u�v�bQ��Q>T�Z��.�B��y,<�Q�9�cP'��u����}1���ht�O�O�$�y���;��DS[6�iGB���^��b��v����ޯR�lOo9�q�s�R����N��?��         �   x�E�AN�@E��)r�:�g<���t�N�3�,���5BT��H�A�
����,y����yh���
0��C��z�O�ʻ|����CEW˔&$#Ѷ�Q�#F(=D
ܦ޴�Ly�Q����7?��җ<��VU��N�u�G=��U�Uj}/Y���l��6�t�Ĥ�t��缝	�Ϥ?W���H��L�L� ZAh8��ыm��W�������|���,��فA           x��Z��-�<�F��"$s� �8l�3�t�\v)�UFn��q������oA �F�����kO٪%��i�\S�dջ������I�\i��ɥ���h�(�pʚr��Fh_i��I��4��ɸ�]�ZB���_�������~�����ӯ����?������럟2���J�:%�U�Qmi��*}����a�R��
<`OVU�ID&�m,����������t��Y��ݸ&�XCtE!�������>x�2B���')R�(s&Z�����������26�b+y� �2�����I��<m>O5)������p̼}\����4FxS^ѓ0�;�D�f�m3��V+��9c������mz�^��l%e�h;O�7�ީ�n�g���ŭ�$��p��ڕ�M��޴��<&<�W��J�D/Q�ܘ������qAa5�� J&��U�ǜd����*��L��lpe��x�Ͷ�[*5��.�' �b{����T��{L�^�i�%�F��&���k�d�?�Ս�K���^�	JР�+�9���{�@�I*��X�a �E+q۹G|���"pI'l�zN+י��}���^�p��2x ,F�ܛf��%���d�<Ԑ���A���v�����l��H(`�.p��TF�]����\��TT�K��#�::�0B���M]�亘Fn3�A]A�4���&k)s�Vs�P�}�*uI�*z���uk�Y#��o�ܧp��G��`L��rU�.���_���!����pCR�"pt�ȪW$>e�p�^��4T�j�BM�w�jx�%����v���.��r��c�s0��B��\(u� ��m�]�M#�Y�NVN�zT��"���=�J��a3�5s{$V�s�H#X&��{G�W���4�hVM{i>�D���D'H�±I�`��t����Bm�S �!�"��[F�*qG0�M�!�UЉ�P�K	¡r��zP��%�<6�r���D�(=	?�~�o���iU���7����w;Mݵ��H����橴�}��	8��eɬ6���s�ɥǦ+C��l�#����T���r��56|�zbY��2�����ԄƝ\zl��3��L��^�������s�#Ӭ!�W?A��K����jm�;��������<i��Y൶Vhm�v9�=5�����~4NH��U!�y�m��3��O����D����bl������4��dB�V=�6OU�8J(f����SӖa�:#���Y/���^a�����0ʹV�� 
`��xQIP�N�����M]_�ޔѻ�<�F�9_g�F���X/!|����i!o3�"q�x��+^�5�F��$G@��Sw��ޛ���߁���W6�(B��3�!�},��|	fmt@y��[� !;X�PMv ]�K0[�m�v$3���B�C�9�Z�ք��a�c"e.k�s������t�8�T���߱��@�0=&�]���T(y$tc�
G8o҅��$O���5�l�vZ��yk���7�3�I��5Aj���	���D�}^��Ӑ_�����.���U��IT
M��a�h��z�B�"�ڏ�Xհ��2���KD:�.�@�V��t^���FG�~I*`�^�#�z�>Q�V�!-��_�p(�J��Y�}�늿V.�������U� �Nq�"|�_�����_�p����s/�:1}��9Ɖ�r�"}����

�(3&'(�S�}�j��KDj>\;�I����s�ԭ��%_�� ��[)<�d�4�\��A`���KDJR��:r)P����/�:Jt�0]H�d�2�"b����
����5��6j��y�d�P�{W���eq=��Hנud	�S�CB<�_c�G0,�,������^��:QoH(��p��x���z|ᆍ�Eg��� � ��1�K!���zj���2��>wkx�p�0r�/�-�����* 3Д�i��4!��pyCI\�Sӄ�z����cz8�����V������ε����*���w������C^�R�L������H�!)�ڰң/�'ׯ��������}������!Y��fl���Q飠�E�[��:!�^��=6��Fʳ�?P����k棌&�)����ABP�;C�=�K�Z�ۢ�
�e,�f6ȵ�D��?�K䚩���]��u-��o�u�/�kgF9�N�r�����LIE�<m�����h�8����_�2ʦC*�C����b\������)��6�����U��h�h��*��QZdS���A("��F1:w_x�o�^.ޘ��W����A)�T����"�>��9�y췼F�Z�PML��9߀�0tj.jy�H��tڇH���w�h�A�jD���ׁB����m��,8�E�g]�4�KD�)��+(.����]�x��zi��t�e�YS�R�Ch����FQ�F�/��>���n��3�~&ԚL�@P�����%,���Q0 ء4�5L����ج�<��-�q�50eiiuFr�7��J���"z��azVi/�-�d��x0�:�~�f H����HO�zI)���}�Z=����Y:���2�-�2�R���r�մ(Cډ%��F#�6�}I)OM�:��AP?7^��`��V��,�p.)������W;0ӡX?(q��be44�����c@'�<R�i�xt8���b��W /L'�%�@-r^"X���0O�� �u�%�<5�2<����BӤ"�u���R����VB}�H�O�^�3�\�y��R����???��nBN         ~   x�ɻ�0 �:�"��lKe� �d�@�i��p�e�hk	�[C���*�H��v}�z8[-'n)�F��'bW�Vo� %�W�8U2R��R�D�Ef*�S���=�����Z�/]��~�_(%�           x�m�KJA��ӧ��"%�~��M�t�9�"n\��/�A0	$3&g����� (��n�����,U��B�܃6�@�� �V&}�Vw8o�8�;q�=>�'n�U��n{��	�8�.p�+��**����I��T!P�)<;/#p��)��
)����.����o�z|"���\��z�-�SxOaF&�� d�@ǒ c�G���C�\t�L�74�������V���״ŒD뿚cX���pK�k[9�(�(��V�\TG���4#1�6���p�6���7V)
�8g�}[���     