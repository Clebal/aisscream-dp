start transaction;

use `Acme-Chollos-Rifas`;

revoke all privileges on `Acme-Chollos-Rifas`.* from 'acme-user'@'%';

revoke all privileges on `Acme-Chollos-Rifas`.* from 'acme-manager'@'%';

drop user 'acme-user'@'%';

drop user 'acme-manager'@'%';

drop database if exists `Acme-Chollos-Rifas`;

commit;