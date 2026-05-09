// lib/features/language/views/language_picker_sheet.dart

import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:prophets_scroll/core/constants/constants.dart';

import '../../../core/theme/app_colors.dart';
import '../../../core/theme/app_text_styles.dart';

class LanguagePickerSheet extends StatefulWidget {
  final String currentLang;
  final void Function(String code) onSelect;

  const LanguagePickerSheet({
    super.key,
    required this.currentLang,
    required this.onSelect,
  });

  @override
  State<LanguagePickerSheet> createState() => _LanguagePickerSheetState();
}

class _LanguagePickerSheetState extends State<LanguagePickerSheet> {
  late String _selected;

  @override
  void initState() {
    super.initState();
    _selected = widget.currentLang;
  }

  void _apply() {
    widget.onSelect(_selected);
    Get.back();
  }

  @override
  Widget build(BuildContext context) {
    return SafeArea(
      child: Padding(
        padding: EdgeInsets.only(
          bottom: MediaQuery.of(context).viewInsets.bottom,
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            _DragHandle(),
            _SheetHeader(),
            const Divider(height: 1, thickness: 0.8),
            _LanguageList(
              selected: _selected,
              onChanged: (code) => setState(() => _selected = code),
            ),
            const Divider(height: 1, thickness: 0.8),
            _FootNote(),
            _ApplyButton(
              onTap: _apply,
              isChanged: _selected != widget.currentLang,
            ),
            const SizedBox(height: 8),
          ],
        ),
      ),
    );
  }
}

// ─── Drag handle ──────────────────────────────────────────────

class _DragHandle extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 12, bottom: 10),
      child: Container(
        width: 36,
        height: 4,
        decoration: BoxDecoration(
          color: AppColors.cardBorder,
          borderRadius: BorderRadius.circular(2),
        ),
      ),
    );
  }
}

// ─── Header ───────────────────────────────────────────────────

class _SheetHeader extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.fromLTRB(16, 0, 16, 12),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text('Select Language', style: AppTextStyles.h3),
          const SizedBox(height: 3),
          Text(
            'Devotional and song switch to your chosen language.',
            style: AppTextStyles.caption,
          ),
        ],
      ),
    );
  }
}

// ─── Language list ────────────────────────────────────────────

class _LanguageList extends StatelessWidget {
  final String selected;
  final void Function(String code) onChanged;

  const _LanguageList({required this.selected, required this.onChanged});

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisSize: MainAxisSize.min,
      children: AppConstants.languages.map((lang) {
        final isActive = lang.code == selected;
        return _LanguageRow(
          lang: lang,
          isActive: isActive,
          onTap: () => onChanged(lang.code),
        );
      }).toList(),
    );
  }
}

// ─── Single language row ──────────────────────────────────────

class _LanguageRow extends StatelessWidget {
  final AppLanguage lang;
  final bool isActive;
  final VoidCallback onTap;

  const _LanguageRow({
    required this.lang,
    required this.isActive,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return InkWell(
      onTap: onTap,
      child: Container(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        decoration: BoxDecoration(
          color: isActive
              ? AppColors.gold.withOpacity(0.06)
              : Colors.transparent,
          border: const Border(
            bottom: BorderSide(color: AppColors.cardBorder, width: 0.8),
          ),
        ),
        child: Row(
          children: [
            // Flag / initials avatar
            _LangAvatar(lang: lang),
            const SizedBox(width: 12),

            // Name + native name
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    lang.label,
                    style: AppTextStyles.body.copyWith(
                      fontWeight: FontWeight.w500,
                      fontSize: 13,
                      color: isActive
                          ? AppColors.textPrimary
                          : AppColors.textSecond,
                    ),
                  ),
                  Text(
                    lang.nativeName,
                    style: AppTextStyles.caption,
                  ),
                ],
              ),
            ),

            // Checkmark when active
            if (isActive)
              const Icon(
                Icons.check_rounded,
                color: AppColors.success,
                size: 18,
              ),
          ],
        ),
      ),
    );
  }
}

// ─── Flag / initials avatar ───────────────────────────────────

class _LangAvatar extends StatelessWidget {
  final AppLanguage lang;
  const _LangAvatar({required this.lang});

  @override
  Widget build(BuildContext context) {
    // Emoji flag — render as text inside a circle
    // For Kikuyu and Dholuo we show initials since they share 🇰🇪 with Swahili
    final showInitials = lang.code == 'ki' || lang.code == 'lu';

    return Container(
      width: 36,
      height: 36,
      decoration: BoxDecoration(
        shape: BoxShape.circle,
        color: showInitials ? AppColors.navy : AppColors.surface,
        border: Border.all(color: AppColors.cardBorder),
      ),
      child: Center(
        child: showInitials
            ? Text(
                lang.code.toUpperCase(),
                style: AppTextStyles.caption.copyWith(
                  color: Colors.white,
                  fontWeight: FontWeight.w600,
                  fontSize: 11,
                ),
              )
            : Text(
                lang.flag,
                style: const TextStyle(fontSize: 18),
              ),
      ),
    );
  }
}

// ─── Footnote ─────────────────────────────────────────────────

class _FootNote extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 10),
      child: Text(
        'The devotional song also switches to the selected language when available.',
        style: AppTextStyles.caption.copyWith(
          fontStyle: FontStyle.italic,
          fontSize: 10,
        ),
        textAlign: TextAlign.center,
      ),
    );
  }
}

// ─── Apply button ─────────────────────────────────────────────

class _ApplyButton extends StatelessWidget {
  final VoidCallback onTap;
  final bool isChanged; // dims the button if user picked the same language

  const _ApplyButton({required this.onTap, required this.isChanged});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16),
      child: GestureDetector(
        onTap: onTap,
        child: AnimatedContainer(
          duration: const Duration(milliseconds: 200),
          height: 48,
          width: double.infinity,
          decoration: BoxDecoration(
            color: isChanged ? AppColors.gold : AppColors.gold.withOpacity(0.45),
            borderRadius: BorderRadius.circular(10),
          ),
          alignment: Alignment.center,
          child: Text(
            'Apply Language',
            style: AppTextStyles.body.copyWith(
              color: AppColors.navy,
              fontWeight: FontWeight.w600,
            ),
          ),
        ),
      ),
    );
  }
}