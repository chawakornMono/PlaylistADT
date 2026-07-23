import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Playlist — ADT แทนรายการเพลงที่ผู้ใช้จัดลำดับไว้
 *
 * ค่านามธรรม (A): ลำดับของเพลง เช่น [เพลงA, เพลงB, เพลงC]
 *
 * ตัวอย่างการใช้งาน:
 *     Playlist p = new Playlist();
 *     p.add("Bohemian Rhapsody");
 *     p.add("Imagine");
 *     System.out.println(p.size());   // 2
 */
public class Playlist {

    public static final int MAX_SONGS = 100;

    // ===== representation =====
    private final List<String> songs;

    // TODO 1: เขียน Abstraction Function ตรงนี้
    // Abstraction Function:
    //   AF(songs) = เพลย์ลิสต์ที่เล่นเพลง songs.get(0), songs.get(1), ...,
    //               songs.get(songs.size()-1) คือเพลงแรกในเพลย์ลิสต์
    //               คือเพลงที่เล่นก่อน และเพลงสุดท้ายในลิสต์คือเพลงที่เล่นหลังสุด

    // TODO 2: เขียน Representation Invariant ตรงนี้ (4 ข้อ)
    // Representation Invariant:
    //   1. songs != null                                  (ต้องมีรายการเพลงอยู่จริง)
    //   2. ไม่มีสมาชิกใน songs เป็น null                    (ไม่มีเพลงใดเป็น null)
    //   3. ไม่มีสมาชิกใน songs เป็นสตริงว่าง "" และ ไม่มีสมาชิกใน songs ซ้ำกัน       (ไม่มีชื่อเพลงที่เป็นสตริงว่างหรือชื่อเพลงห้ามซ้ำกัน)
    //   4. songs.size() <= MAX_SONGS                       (มีได้ไม่เกิน MAX_SONGS เพลง)

    // TODO 3: เขียน Safety from rep exposure ตรงนี้
    // Safety from rep exposure:
    //   - songs ถูกประกาศเป็น private final จึงไม่มีการเปลี่ยนของ field นี้ได้จากภายนอก
    //   - constructor ที่รับ List<String> จากภายนอก จะคัดลอกข้อมูลใส่ ArrayList ใหม่
    //     แทนที่จะเก็บ ของ list ที่ส่งมาโดยตรง ดังนั้นการแก้ list ต้นฉบับ
    //     ของ client ในภายหลังจะไม่กระทบ rep 
    //   - เมธอด songs() (ขาออก) คืนค่าเป็นสำเนา (copy) ของ songs ไม่ใช่ reference ตรง ๆ
    //     ดังนั้นผู้เรียกจะแก้ไข list ที่คืนกลับไปไม่ส่งผลต่อ rep ภายใน
    //   - String เป็น immutable อยู่แล้ว จึงไม่ต้องกังวลเรื่องการแก้ไของค์ประกอบภายใน list
 
    /**
     * TODO 4: เขียน checkRep()
     * แปลง RI ทุกข้อเป็น assert หนึ่งบรรทัด พร้อมข้อความอธิบาย
     */
    private void checkRep() {
        // เขียนโค้ดตรงนี้
        assert songs != null : "songs ต้องไม่เป็น null";
        Set<String> seen = new HashSet<>();
        for (String s : songs) {
            assert s != null : "songs ต้องไม่มีสมาชิกเป็น null";
            assert !s.isEmpty() : "songs ต้องไม่มีสมาชิกเป็นสตริงว่าง";
            assert seen.add(s) : "ชื่อเพลงซ้ำ: " + s;
        }
        assert songs.size() <= MAX_SONGS : "songs ต้องมีจำนวนไม่เกิน MAX_SONGS";
    }

    // ===== Creator =====

    /**
     * สร้างเพลย์ลิสต์ว่าง
     */
    public Playlist() {
        this.songs = new ArrayList<>();
        checkRep();
    }

    /**
     * TODO 5: Creator ตัวที่สอง
     * สร้างเพลย์ลิสต์จากรายชื่อเพลงที่ให้มา
     *
     * ระวัง: ห้ามเก็บ reference ของ initial ตรง ๆ (rep exposure!)
     *
     * @param initial รายชื่อเพลงเริ่มต้น ต้องไม่ซ้ำและไม่เกิน MAX_SONGS
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    public Playlist(List<String> initial) {
       if (initial == null) {
            throw new IllegalArgumentException("initial must not be null");
        }
        for (String s : initial) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("initial must not contain null or empty songs");
            }
        }
        if (new HashSet<>(initial).size() != initial.size()) {
            throw new IllegalArgumentException("initial must not contain duplicate songs");
        }
        if (initial.size() > MAX_SONGS) {
            throw new IllegalArgumentException("initial must not exceed MAX_SONGS");
        }
 
        this.songs = new ArrayList<>(initial); // copy, ไม่เก็บ reference ตรง ๆ
        checkRep();
    }

    // ===== Mutators =====

    /**
     * TODO 6: เพิ่มเพลงต่อท้ายเพลย์ลิสต์
     *
     * @param song ชื่อเพลง ต้องไม่เป็น null และไม่เป็นสตริงว่าง
     * @return true ถ้าเพิ่มสำเร็จ, false ถ้ามีเพลงนี้อยู่แล้วหรือเต็มแล้ว
     * @throws IllegalArgumentException ถ้า song เป็น null หรือสตริงว่าง
     */
    public boolean add(String song) {
         if (song == null || song.isEmpty()) {
            throw new IllegalArgumentException("song must not be null or empty");
        }
        if (songs.contains(song) || songs.size() >= MAX_SONGS) {
            return false;
        }
        songs.add(song);
        checkRep();
        return true;
    }
    /**
     * TODO 7: ลบเพลงออกจากเพลย์ลิสต์
     *
     * @param song ชื่อเพลงที่ต้องการลบ
     * @return true ถ้าลบสำเร็จ, false ถ้าไม่พบเพลงนี้
     */
    public boolean remove(String song) {
        boolean removed = songs.remove(song);
        checkRep();
        return removed;
    }

    // ===== Observers =====

    /**
     * TODO 8: คืนจำนวนเพลงในเพลย์ลิสต์
     */
    public int size() {
         return songs.size();
    }

    /**
     * TODO 9: ตรวจว่ามีเพลงนี้อยู่หรือไม่
     */
    public boolean contains(String song) {
           return songs.contains(song);
    }

    /**
     * TODO 10: คืนรายชื่อเพลงทั้งหมดตามลำดับ
     *
     * ระวัง: ห้ามคืน reference ของ songs ตรง ๆ (rep exposure!)
     */
    public List<String> songs() {
       return new ArrayList<>(songs);
    }

    // ===== Producer =====

    /**
     * TODO 11: คืนเพลย์ลิสต์ใหม่ที่มีเพลงเดียวกันแต่สลับลำดับ
     *
     * ระวัง: ห้ามแก้เพลย์ลิสต์เดิม (this) เด็ดขาด
     *
     * @return เพลย์ลิสต์ใหม่ที่สลับลำดับแล้ว
     */
    public Playlist shuffled() {
        List<String> copy = new ArrayList<>(songs);
        Collections.shuffle(copy);
        return new Playlist(copy);
    }

    @Override
    public String toString() {
        return songs.toString();
    }
}
