start transaction;

use `Acme-Newspaper`;

revoke all privileges on `Acme-Newspaper`.* from 'acme-user'@'%';

revoke all privileges on `Acme-Newspaper`.* from 'acme-manager'@'%';

drop user 'acme-user'@'%';

drop user 'acme-manager'@'%';

drop database if exists `Acme-Newspaper`;

commit;