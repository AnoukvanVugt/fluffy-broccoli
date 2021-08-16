--
-- Name: fluffy-broccoli; Type: SCHEMA; Schema: -; Owner: fluffy_owner
--

CREATE SCHEMA fluffy_owner;


ALTER SCHEMA fluffy_owner OWNER TO fluffy_owner;

--
-- Name: wrn_abonnement; Type: TABLE; Schema: fluffy_owner; Owner: fluffy_owner
--

CREATE TABLE fluffy_owner.wrn_abonnement (
    id text NOT NULL,
    name text,
    date_entered bigint,
    date_modified bigint,
    modified_user_id text,
    created_by text,
    description text,
    deleted smallint DEFAULT 0,
    team_id text,
    team_set_id text,
    assigned_user_id text,
    type_abonnement text,
    abonnement_code text,
    type_lid_abonnement text,
    lid smallint DEFAULT 0,
    vestiging smallint DEFAULT 0,
    vanaf_aantal_leden integer,
    tot_en_met_aantal_leden integer,
    wrn_ring_id text,
    datum_start integer,
    datum_eind integer
);


ALTER TABLE fluffy_owner.wrn_abonnement OWNER TO fluffy_owner;

--
-- Name: wrn_abonnement_afname; Type: TABLE; Schema: fluffy_owner; Owner: fluffy_owner
--

CREATE TABLE fluffy_owner.wrn_abonnement_afname (
    id text NOT NULL,
    name text,
    date_entered bigint,
    date_modified bigint,
    modified_user_id text,
    created_by text,
    description text,
    deleted smallint DEFAULT 0,
    wrn_abonnement_id text NOT NULL,
    wrn_lid_id text,
    wrn_vestiging_id text,
    per_adres_organisatie_wrn_vestiging_id text,
    team_id text,
    team_set_id text,
    assigned_user_id text,
    datum_start integer,
    datum_eind integer
);


ALTER TABLE fluffy_owner.wrn_abonnement_afname OWNER TO fluffy_owner;

--
-- Name: wrn_abonnement_afname_audit; Type: TABLE; Schema: fluffy_owner; Owner: fluffy_owner
--

CREATE TABLE fluffy_owner.wrn_abonnement_afname_audit (
    id text NOT NULL,
    parent_id text NOT NULL,
    event_id text NOT NULL,
    date_created bigint,
    created_by text,
    date_updated bigint,
    field_name text,
    data_type text,
    before_value_string text,
    after_value_string text,
    before_value_text text,
    after_value_text text
);


ALTER TABLE fluffy_owner.wrn_abonnement_afname_audit OWNER TO fluffy_owner;

--
-- Name: wrn_abonnement_afname_audit wrn_abonnement_afname_audit_pkey; Type: CONSTRAINT; Schema: fluffy_owner; Owner: fluffy_owner
--

ALTER TABLE ONLY fluffy_owner.wrn_abonnement_afname_audit
    ADD CONSTRAINT wrn_abonnement_afname_audit_pkey PRIMARY KEY (id);


--
-- Name: wrn_abonnement_afname wrn_abonnement_afname_pkey; Type: CONSTRAINT; Schema: fluffy_owner; Owner: fluffy_owner
--

ALTER TABLE ONLY fluffy_owner.wrn_abonnement_afname
    ADD CONSTRAINT wrn_abonnement_afname_pkey PRIMARY KEY (id);


--
-- Name: wrn_abonnement wrn_abonnement_pkey; Type: CONSTRAINT; Schema: fluffy_owner; Owner: fluffy_owner
--

ALTER TABLE ONLY fluffy_owner.wrn_abonnement
    ADD CONSTRAINT wrn_abonnement_pkey PRIMARY KEY (id);
