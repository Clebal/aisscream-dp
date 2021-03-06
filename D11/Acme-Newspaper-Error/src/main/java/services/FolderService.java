﻿
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.FolderRepository;
import security.LoginService;
import domain.Actor;
import domain.Folder;
import domain.Message;

@Service
@Transactional
public class FolderService {

	// Managed repository -----------------------------------------------------
	@Autowired
	private FolderRepository		folderRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private MessageService			messageService;

	@Autowired
	private Validator				validator;

	// Simple CRUD methods
	// -----------------------------------------------------------

	public Folder create(final Actor actor) {
		Folder result;
		Set<Folder> folders;

		result = new Folder();
		folders = new HashSet<Folder>();
		
		result.setActor(actor);
		result.setChildrenFolders(folders);
		result.setSystem(false);

		return result;
	}

	public Collection<Folder> findAll() {
		Collection<Folder> result;

		result = this.folderRepository.findAll();

		return result;
	}

	public Folder findOne(final int folderId) {
		Folder result;

		Assert.isTrue(folderId != 0);

		result = this.folderRepository.findOne(folderId);

		return result;
	}

	public Folder save(final Folder folder) {
		Folder result;
		Folder saved;
		// boolean checkSpamWords;
		List<String> listFatherFolders;
		Folder folderCopy;
		List<Folder> listChildrenFolders;
		boolean checkTree;
		Collection<Folder> foldersActor;
		Actor actor;

		Assert.notNull(folder, "folder.notNull");
		saved = null;
		// checkSpamWords = this.checkSpamWords(folder);

		/*
		 * si no es la primera vez que se registra, no puede modificar
		 * parametros
		 */
		// checkTree = false;
		// folder1 = new ArrayList<String😠);
		// folder2 = folder;
		// folder3 = new ArrayList<Folder😠);
		//
		// while (folder2.getFatherFolder() != null) {
		// folder1.add(folder2.getFatherFolder().getName());
		// folder2 = folder2.getFatherFolder();
		// folder3.add(folder2.getFatherFolder());
		// }
		//
		// for (final Folder c : folder.getFatherFolder().getChildrenFolders())
		// if (c.getId() != folder.getId())
		// folder1.add(c.getName());
		// Assert.isTrue(!folder1.contains(folder.getName()));
		//
		// for (final Folder c1 : folder3) {
		// checkTree = folder.getChildrenFolders().contains(c1);
		// if (checkTree)
		// break;
		// }

		actor = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(actor, "folder.logged");
		Assert.isTrue(actor.equals(folder.getActor()), "folder.actor.equals");

		checkTree = false;
		listFatherFolders = new ArrayList<String>();
		folderCopy = folder;
		listChildrenFolders = new ArrayList<Folder>();

		while (folderCopy.getFatherFolder() != null) {
			//listFatherFolders.add(folderCopy.getFatherFolder().getName());
			listChildrenFolders.add(folderCopy.getFatherFolder());
			folderCopy = folderCopy.getFatherFolder();
		}

		if (folder.getFatherFolder() != null) {
			// Crear una lista de father folder y ver que no contenta a la
			// folder en cuestion
			for (final Folder c : folder.getFatherFolder().getChildrenFolders())
				if (c.getId() != folder.getId())
					listFatherFolders.add(c.getName());
			Assert.isTrue(!listFatherFolders.contains(folder.getName()), "folder.name.brother");
			// Crear una lista de children folder y ver que no contenta a la
			// folder en cuestion
			for (final Folder folderP : listChildrenFolders) {
				checkTree = folder.getChildrenFolders().contains(folderP);
				if (checkTree) {
					Assert.isTrue(folder.getChildrenFolders().contains(folderP), "folder.childrens.father");
					break;
				}
			}
		}

		// No puede existir 2 carpetas con el mismo nombre en el mismo
		// directorio

		foldersActor = this.folderRepository.findByActorId(folder.getActor().getId());

		for (final Folder f : foldersActor)
			if (folder.getFatherFolder() == null && f.getFatherFolder() == null && f != folder)
				Assert.isTrue(!folder.getName().equals(f.getName()), "folder.directory");

		// Un actor solo puede crear carpetas para si mismo y no pueden ser del
		// Sistema
		// Mirar que si se peristen/crear o cambian carpetas del sistema sea un
		// actor que todavía no se ha guardado
		// Hay que tener en cuenta que no se pueden modificar las del sistema,
		// pero si se pueden meter mensajes en ella.
		// Los actores no pueden borrar, modificar o mover las carpetas del
		// sistema

		// Si es una carpeta del sistema no puede modificarla
		if (folder.getId() != 0) {
			// Assert.isTrue(folder.getActor().getFolders().contains(folder));
			Assert.isTrue(this.folderRepository.findByActorId(folder.getActor().getId()).contains(folder), "folder.actor.equals");
			if (folder.getSystem()) {
				saved = this.findOne(folder.getId());
				Assert.isTrue(saved.getSystem(), "folder.equals.system");
				Assert.isTrue(saved.getName().equals(folder.getName()), "folder.equals.name");
				if (saved.getFatherFolder() != null)
					Assert.isTrue(saved.getFatherFolder().equals(folder.getFatherFolder()), "folder.equals.fatherFolder");
			}
		}

		// if (folder.getActor().getFolders().size() >= 5)
		if (this.folderRepository.findByActorId(folder.getActor().getId()).size() >= 5)
			if (folder.getActor().getId() != 0)
				if (folder.getId() == 0)
					Assert.isTrue(!folder.getSystem(), "folder.equals.notSystem");

		result = this.folderRepository.save(folder);

		return result;
	}

	public void delete(final Folder folder) {
		Folder saved;
		
		Assert.notNull(folder);
		
		saved = this.folderRepository.findOne(folder.getId());

		Assert.isTrue(!saved.getSystem());

		for (final Folder folders : saved.getChildrenFolders())
			this.delete(folders);

		for (final Message message : this.messageService.findByFolderId(saved.getId()))
			this.messageService.delete(message);

		this.folderRepository.delete(saved);

	}

	// Other business methods -------------------------------------------------

	public void deleteSystemFolder(final Folder folder) {

		Assert.notNull(folder, "folder.notNull");

		for (final Folder folders : folder.getChildrenFolders())
			if (folders.getChildrenFolders().isEmpty())
				this.folderRepository.delete(folders);
			else
				this.delete(folders);

		this.folderRepository.delete(folder);

	}

	public Collection<Folder> findByActorId(final int actorId) {
		Collection<Folder> result;

		Assert.notNull(LoginService.getPrincipal());

		result = this.folderRepository.findByActorId(actorId);

		return result;
	}
	
	public Page<Folder> findByActorUserAccountId(final int userAccountId, final int page, final int size) {
		Page<Folder> result;
		
		Assert.isTrue(userAccountId != 0);
		
		result = this.folderRepository.findByActorUserAccountId(userAccountId, this.getPageable(page, size));
		
		return result;
	}

	public void createDefaultFolders(final Actor actor) {
		Folder folder1, folder2, folder3, folder4, folder5;

		folder1 = this.create(actor);
		folder1.setName("in box");
		folder1.setSystem(true);
		this.folderRepository.save(folder1);

		folder2 = this.create(actor);
		folder2.setName("out box");
		folder2.setSystem(true);
		this.folderRepository.save(folder2);

		folder3 = this.create(actor);
		folder3.setName("trash box");
		folder3.setSystem(true);
		this.folderRepository.save(folder3);

		folder4 = this.create(actor);
		folder4.setName("spam box");
		folder4.setSystem(true);
		this.folderRepository.save(folder4);
		
		folder5 = this.create(actor);
		folder5.setName("notification box");
		folder5.setSystem(true);
		this.folderRepository.save(folder5);

	}

	public Folder findByActorIdAndFolderName(final int actorId, final String folderName) {
		Folder result;

		Assert.isTrue(actorId != 0);
		Assert.isTrue(folderName != null);

		result = this.folderRepository.findByActorIdAndFolderName(actorId, folderName);

		return result;
	}
	
	public Page<Folder> findChildrenFoldersByFolderId(final int folderId, final int page, final int size) {
		Page<Folder> result;
		
		Assert.isTrue(folderId != 0);
		
		result = this.folderRepository.findChildrenFoldersByFolderId(folderId, this.getPageable(page, size));
		
		return result;
	}
	
	// Auxiliary methods
	private Pageable getPageable(final int page, final int size) {
		Pageable result;
		
		if (page == 0 || size <= 0)
			result = new PageRequest(0, 5);
		else
			result = new PageRequest(page - 1, size);
		
		return result;
	}
	
	public Folder reconstruct(final Folder folder, final BindingResult binding) {
		Folder saved;
		Collection<Folder> childrenFolders;
		
		childrenFolders = new ArrayList<Folder>();
		
		if (folder.getId() == 0) {
			folder.setSystem(false);
			folder.setChildrenFolders(childrenFolders);
		} else {
			saved = this.folderRepository.findOne(folder.getId());
			folder.setSystem(saved.getSystem());
			folder.setActor(saved.getActor());
			folder.setVersion(saved.getVersion());
			folder.setChildrenFolders(saved.getChildrenFolders());
			folder.setFatherFolder(saved.getFatherFolder());
		}

		this.validator.validate(folder, binding);

		return folder;
	}

}
